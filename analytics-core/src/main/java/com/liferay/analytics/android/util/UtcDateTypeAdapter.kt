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

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Victor Oliveira
 */
class UtcDateTypeAdapter(dateFormat: String) : JsonSerializer<Date>, JsonDeserializer<Date> {
	private val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())

	init {
		simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
	}

	@Synchronized
	override fun serialize(
		date: Date, type: Type, context: JsonSerializationContext): JsonElement {

		return JsonPrimitive(simpleDateFormat.format(date))
	}

	@Synchronized
	@Throws(ParseException::class)
	override fun deserialize(
		jsonElement: JsonElement, type: Type, context: JsonDeserializationContext): Date {

		try {
			return simpleDateFormat.parse(jsonElement.asString)
		} catch (e: ParseException) {
			throw JsonParseException(e)
		}
	}
}