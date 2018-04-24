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

package com.liferay.analytics.client.android.model

import com.liferay.analytics.model.AnalyticsEventsMessage

import java.util.Date
import java.util.HashMap

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class EventModel(event: AnalyticsEventsMessage.Event) {

	var applicationId: String
	var eventDate = Date()
	var eventId: String
	var properties: Map<String, String> = HashMap()

	init {
		applicationId = event.applicationId
		eventDate = event.eventDate
		eventId = event.eventId
		properties = event.properties
	}

}