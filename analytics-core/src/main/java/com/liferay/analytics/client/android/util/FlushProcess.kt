/*
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

package com.liferay.analytics.client.android.util

import android.content.Context
import com.liferay.analytics.client.android.Analytics
import com.liferay.analytics.client.android.api.impl.AnalyticsClientImpl
import com.liferay.analytics.client.android.dao.EventsDAO
import com.liferay.analytics.client.android.model.AnalyticsEventsMessage
import com.liferay.analytics.client.android.model.Event
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author Igor Matos
 */
internal class FlushProcess(context: Context, var interval: Long) {

	var eventsQueue: MutableList<Event> = mutableListOf()
	var isInProgress = false
	var eventsDAO: EventsDAO = EventsDAO(context)

	init {
		flush()
	}

	private fun sendEvents() {
		if (isInProgress) {
			return
		}

		try {
			isInProgress = true

			val instance = Analytics.instance!!
			val userId = "userIdAndroid"

			var events = getEventsToSend()

			while (events.isNotEmpty()) {
				val analyticsEventsMessage = AnalyticsEventsMessage(instance.analyticsKey, userId)

				analyticsEventsMessage.events = events.take(100).toMutableList()

				analyticsClientImpl.sendAnalytics(analyticsEventsMessage)

				events = getRemainingEvents()
				eventsDAO.replace(events)
			}

		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			isInProgress = false
			saveEventsQueue()
		}
	}

	fun getEventsToSend(): List<Event> {
		return eventsDAO.events.take(FLUSH_SIZE)
	}

	fun getRemainingEvents(): List<Event> {
		return eventsDAO.events.drop(FLUSH_SIZE)
	}

	fun saveEventsQueue() {
		eventsDAO.addEvents(eventsQueue)
		eventsQueue.clear()
	}

	fun addEvent(event: Event) {
		if (isInProgress) {
			eventsQueue.add(event)

			return
		}

		eventsDAO.addEvents(listOf(event))
	}

	private fun flush() {
		Observable.interval(interval, interval, TimeUnit.MILLISECONDS)
			.subscribeOn(Schedulers.io())
			.subscribe {
				sendEvents()
			}
	}

	private val analyticsClientImpl = AnalyticsClientImpl()

	companion object {
		private const val FLUSH_SIZE = 100
	}

}