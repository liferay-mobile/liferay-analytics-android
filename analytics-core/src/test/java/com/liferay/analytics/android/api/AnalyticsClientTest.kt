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

package com.liferay.analytics.android.api

import com.liferay.analytics.android.BaseTest
import com.liferay.analytics.android.model.AnalyticsEvents
import com.liferay.analytics.android.model.Event
import com.liferay.analytics.android.util.JSONParser
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsClientTest: BaseTest() {

	private lateinit var userId: String

	@Before
	fun setUp() {
		userId = getUserId()
	}

	@Test
	@Throws(Exception::class)
	fun sendAnalytics() {
		val server = MockWebServer()
		server.enqueue(MockResponse().setBody("{}").setResponseCode(200))
		val mockedURL = server.url("/")

		val event = Event("ApplicationId", "EventId")

		val analyticsEvents = AnalyticsEvents("dataSourceId", userId).apply {
			protocolVersion = "1.0"
			events = mutableListOf(event)
		}

		AnalyticsClient().sendAnalytics(mockedURL.toString(), analyticsEvents)

		val request = server.takeRequest()
		val requestBody = JSONObject(request.body.readUtf8())

		val contextJSON = requestBody.getJSONObject("context")
		Assert.assertTrue(contextJSON.has("userAgent"))
		Assert.assertTrue(contextJSON.has("contentLanguageId"))
		Assert.assertTrue(contextJSON.has("languageId"))
		Assert.assertTrue(contextJSON.has("screenHeight"))
		Assert.assertTrue(contextJSON.has("screenWidth"))

		val simpleDateFormat = SimpleDateFormat(JSONParser.DATE_FORMAT)
		simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
		val eventDate = simpleDateFormat.format(event.eventDate)
		val eventJSON = requestBody.getJSONArray("events").getJSONObject(0)

		Assert.assertTrue(eventJSON.has("properties"))
		Assert.assertEquals("ApplicationId", eventJSON.getString("applicationId"))
		Assert.assertEquals("EventId", eventJSON.getString("eventId"))
		Assert.assertEquals(eventDate, eventJSON.getString("eventDate"))

		Assert.assertEquals("1.0", requestBody.getString("protocolVersion"))
		Assert.assertEquals("dataSourceId", requestBody.getString("dataSourceId"))
		Assert.assertEquals(userId, requestBody.getString("userId"))

		server.shutdown()
	}

	private fun getUserId(): String {
		val currentDate = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())

		return "ANDROID$currentDate"
	}
}