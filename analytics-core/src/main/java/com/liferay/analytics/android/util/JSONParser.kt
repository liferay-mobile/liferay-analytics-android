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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

/**
 * @author Igor Matos
 * @author Allan Melo
 */
internal object JSONParser {

	private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

	@Throws(JsonSyntaxException::class)
	fun <T> fromJsonString(json: String, type: Type): T {
		return gson().fromJson(json, type)
	}

	fun toJSON(element: Any): String {
		return gson().toJson(element)
	}

	internal fun gson(): Gson {
		val gsonBuilder = GsonBuilder()
		gsonBuilder.setDateFormat(DATE_FORMAT)

		return gsonBuilder.create()
	}
}