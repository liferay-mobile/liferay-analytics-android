/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.analytics.android.util

import android.util.Log
import com.liferay.analytics.android.Analytics
import com.liferay.analytics.android.api.AnalyticsClient
import com.liferay.analytics.android.api.IdentityClient
import com.liferay.analytics.android.dao.EventsDAO
import com.liferay.analytics.android.dao.UserDAO
import com.liferay.analytics.android.model.AnalyticsEvents
import com.liferay.analytics.android.model.Event
import com.liferay.analytics.android.model.IdentityContext
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

/**
 * @author Igor Matos
 */
internal class FlushProcess(fileStorage: FileStorage, interval: Long) {

	var eventsQueue: MutableMap<String, List<Event>> = mutableMapOf()
	var isInProgress = false
	var eventsDAO: EventsDAO = EventsDAO(fileStorage)
	var userDAO: UserDAO = UserDAO(fileStorage)
    private val flushInterval = interval * 1000

	init {
		flush()
	}

	fun addEvent(event: Event) {
		val userId = getUserId()

		if (isInProgress) {
			eventsQueue[userId] = (eventsQueue[userId] ?: listOf()).plus(event)

			return
		}

		eventsDAO.addEvents(userId, listOf(event))
	}

	fun getUserId(): String {
		val userId = userDAO.getUserId() ?: initUserId()

		return userId
	}

	internal fun saveEventsQueue() {
		eventsQueue.forEach { (userId, events) ->
			eventsDAO.addEvents(userId, events)
		}

		eventsQueue.clear()
	}

	internal fun sendIdentities() {
		val identityClient = IdentityClient()

		var userContexts = userDAO.getUserContexts().toMutableList()

		while (userContexts.isNotEmpty()) {
			val userContext = userContexts.removeAt(0)

			identityClient.send(userContext)
			userDAO.replace(userContexts)
		}
	}

	private fun flush() {
		Timer().schedule(flushInterval) {
			sendEvents()
			flush()
		}
	}

	private fun initUserId(): String {
		val instance = Analytics.instance!!
		val identityContext = IdentityContext(instance.analyticsKey)

		userDAO.addIdentityContext(identityContext)
		userDAO.setUserId(identityContext.userId)

		return identityContext.userId
	}

	private fun sendEvents() {
		try {
			isInProgress = true

			val userIdsAndEvents = eventsDAO.getEvents()

			userIdsAndEvents.forEach { (userId, events) ->
				sendEventsForUserId(userId, events)
				userIdsAndEvents.remove(userId)

				eventsDAO.replace(userIdsAndEvents)
			}

			sendIdentities()
		}
		catch (e: IOException) {
			Log.d("LIFERAY-ANALYTICS",
				"Could not send analytics events ${e.localizedMessage}")
		}
		finally {
			isInProgress = false
			saveEventsQueue()
		}
	}

	private fun sendEventsForUserId(userId: String, events: List<Event>) {
		val instance = Analytics.instance!!

		var currentEvents = events

		while (currentEvents.isNotEmpty()) {
			val analyticsEvents = AnalyticsEvents(instance.analyticsKey, userId)

			analyticsEvents.events = currentEvents.take(FLUSH_SIZE)

			analyticsClient.sendAnalytics(analyticsEvents)

			currentEvents = currentEvents.drop(FLUSH_SIZE)
			eventsDAO.replace(userId, currentEvents)
		}
	}

	private val analyticsClient = AnalyticsClient()

	companion object {
		private const val FLUSH_SIZE = 100
	}
}