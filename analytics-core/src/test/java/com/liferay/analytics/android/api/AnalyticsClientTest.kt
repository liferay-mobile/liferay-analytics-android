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

import com.liferay.analytics.android.model.AnalyticsEvents
import com.liferay.analytics.android.model.Event
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsClientTest {

	private val analyticsClient = Mockito.spy(AnalyticsClient::class.java)
	private lateinit var userId: String

	@Before
	fun setUp() {
		userId = getUserId()

		Mockito.`when`(analyticsClient.analyticsGatewayHost).thenReturn("ec-dev.liferay.com")
		Mockito.`when`(analyticsClient.analyticsGatewayProtocol).thenReturn("https")
		Mockito.`when`(analyticsClient.analyticsGatewayPort).thenReturn("8095")
		Mockito.`when`(analyticsClient.analyticsGatewayPath).thenReturn("/api/analyticsgateway/send-analytics-events")
	}

	@Test
	@Throws(Exception::class)
	fun sendAnalytics() {
		var analyticsEventsMessage = AnalyticsEvents("analyticsKey", userId).apply {
			context = mapOf("languageId" to "pt_PT")

			protocolVersion = "1.0"

			val event = Event("ApplicationId", "EventId")
			events = mutableListOf(event)
		}

		try {
			analyticsClient.sendAnalytics(analyticsEventsMessage)
		} catch (e: IOException) {
			Assert.fail()
		}
	}

	private fun getUserId(): String {
		val currentDate = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())

		return "ANDROID$currentDate"
	}

}