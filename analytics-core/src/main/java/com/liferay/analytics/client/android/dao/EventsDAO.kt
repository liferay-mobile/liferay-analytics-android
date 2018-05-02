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

package com.liferay.analytics.client.android.dao

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liferay.analytics.client.android.model.EventModel
import com.liferay.analytics.client.android.util.FileStorage
import java.io.IOException
import java.lang.reflect.Type

/**
 * @author Igor Matos
 */
internal class EventsDAO(context: Context) {

	private val fileStorage: FileStorage = FileStorage(context)

	val events: List<EventModel>
		get() {
			val eventsJsonString = fileStorage.getStringByKey(EVENTS_KEY)

			eventsJsonString?.let {
				try {
					return Gson().fromJson<List<EventModel>>(it, listType())
				} catch (e: JsonSyntaxException) {
					clear()
				}
			}

			return listOf()
		}

	fun addEvents(events: List<EventModel>) {
		val eventsToSave = this.events.plus(events)

		replace(eventsToSave)
	}

	fun clear() {
		replace(listOf())
	}

	fun replace(events: List<EventModel>) {
		val eventsJson = Gson().toJson(events)

		try {
			fileStorage.saveStringToKey(EVENTS_KEY, eventsJson)
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	private fun listType(): Type? {
		return object : TypeToken<ArrayList<EventModel>>() {}.type
	}

	companion object {
		private const val EVENTS_KEY = "events"
	}

}