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

import com.google.gson.annotations.SerializedName

import com.liferay.analytics.model.AnalyticsEventsMessage

import java.util.ArrayList
import java.util.HashMap

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsEventsMessageModel(analyticsEventsMessage: AnalyticsEventsMessage) {

    var analyticsKey: String
    var context: Map<String, String> = HashMap()
    var events: MutableList<EventModel> = ArrayList()
    var protocolVersion: String?

    @SerializedName(alternate = ["userid"], value = "userId")
    var userId: String

    init {

        analyticsKey = analyticsEventsMessage.analyticsKey
        context = analyticsEventsMessage.context
        events = ArrayList()

        for (event in analyticsEventsMessage.events) {

            events.add(EventModel(event))
        }

        protocolVersion = analyticsEventsMessage.protocolVersion
        userId = analyticsEventsMessage.userId
    }

}