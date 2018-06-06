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

package com.liferay.analytics.android.dao

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liferay.analytics.android.model.Event
import com.liferay.analytics.android.util.FileStorage
import java.io.IOException
import java.lang.reflect.Type

/**
 * @author Igor Matos
 */
internal class EventsDAO(private var fileStorage: FileStorage) {

	fun addEvents(userId: String, events: List<Event>) {
		val currentEvents = getEvents()
		currentEvents[userId] = (currentEvents[userId] ?: listOf()) + events

		replace(currentEvents)
	}

	fun clear() {
		replace(mutableMapOf())
	}

	fun getEvents(): MutableMap<String, List<Event>> {
		val eventsJsonString = fileStorage.getStringByKey(EVENTS_KEY)

		eventsJsonString?.let {
			try {
				return Gson().fromJson<MutableMap<String, List<Event>>>(it, listType())
			}
			catch (e: JsonSyntaxException) {
				clear()
			}
		}

		return mutableMapOf()
	}

	fun replace(events: MutableMap<String, List<Event>>) {
		val eventsJson = Gson().toJson(events)

		try {
			fileStorage.saveStringToKey(EVENTS_KEY, eventsJson)
		}
		catch (e: IOException) {
			Log.e("LIFERAY-ANALYTICS", "Could not replace events")
		}
	}

	fun replace(userId: String, events: List<Event>) {
		var currentEvents = getEvents()
		currentEvents[userId] = events

		replace(currentEvents)
	}

	private fun listType(): Type? {
		return object : TypeToken<MutableMap<String, List<Event>>>() {}.type
	}

	companion object {
		private const val EVENTS_KEY = "events"
	}
}