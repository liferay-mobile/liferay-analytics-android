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

package com.liferay.analytics.client.android.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liferay.analytics.client.android.model.EventModel
import java.io.FileNotFoundException
import java.io.IOException

/**
 * @author Igor Matos
 */
class EventsDAO(context: Context) {

	private val _fileStorage: FileStorage = FileStorage(context)

	val savedEvents: List<EventModel>?
		get() {
			try {
				val eventsJson = _fileStorage.getStringByKey("events")

				val listType = object : TypeToken<ArrayList<EventModel>>() {

				}.type
				return Gson().fromJson<List<EventModel>>(eventsJson, listType)
			} catch (e: FileNotFoundException) {

			} catch (e: IOException) {
				e.printStackTrace()
			}

			return null
		}

	fun addEvents(events: List<EventModel>) {
		val listType = object : TypeToken<ArrayList<EventModel>>() {

		}.type
		val eventsJson = Gson().toJson(events, listType)

		try {
			_fileStorage.saveStringToKey("events", eventsJson)
		} catch (e: IOException) {
			e.printStackTrace()
		}

	}

}