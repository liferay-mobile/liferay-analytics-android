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

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.liferay.analytics.client.android.model.AnalyticsEventsMessageModel
import com.liferay.analytics.model.AnalyticsEventsMessage
import java.lang.reflect.Type

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsEventsMessageSerializer : JsonSerializer<AnalyticsEventsMessage> {

	override fun serialize(
		analyticsEventsMessage: AnalyticsEventsMessage, typeOfSrc: Type,
		context: JsonSerializationContext): JsonElement {

		val model = AnalyticsEventsMessageModel(analyticsEventsMessage)

		val gson = JSONParser.gson()

		return gson.toJsonTree(model)
	}

}