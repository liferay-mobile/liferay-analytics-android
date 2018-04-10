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

package com.liferay.analytics.client.android.api.impl

import com.liferay.analytics.client.AnalyticsClient
import com.liferay.analytics.client.android.util.JSONParser
import com.liferay.analytics.model.AnalyticsEventsMessage

import java.io.IOException

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsClientImpl : AnalyticsClient {

    val analyticsGatewayHost: String
        get() = ANALYTICS_GATEWAY_HOST

    val analyticsGatewayPath: String
        get() = ANALYTICS_GATEWAY_PATH

    val analyticsGatewayPort: String
        get() = ANALYTICS_GATEWAY_PORT

    val analyticsGatewayProtocol: String
        get() = ANALYTICS_GATEWAY_PROTOCOL

    private val _client = OkHttpClient()

    @Throws(Exception::class)
    override fun sendAnalytics(analyticsEventsMessage: AnalyticsEventsMessage): String {

        val json = JSONParser.toJSON(analyticsEventsMessage)

        val analyticsPath = "$analyticsGatewayProtocol://" +
                "$analyticsGatewayHost:$analyticsGatewayPort" +
                "$analyticsGatewayPath"

        return post(analyticsPath, json)
    }

    @Throws(IOException::class)
    private fun post(url: String, json: String): String {
        val body = RequestBody.create(MEDIA_TYPE, json)

        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        val response = _client.newCall(request).execute()

        return response.body()!!.string()
    }

    companion object {

        private const val ANALYTICS_GATEWAY_HOST = "ec-dev.liferay.com"

        private const val ANALYTICS_GATEWAY_PATH = "/api/analyticsgateway/send-analytics-events"

        private const val ANALYTICS_GATEWAY_PORT = "8095"

        private const val ANALYTICS_GATEWAY_PROTOCOL = "https"

        private val MEDIA_TYPE = MediaType.parse(
                "application/json; charset=utf-8")
    }

}