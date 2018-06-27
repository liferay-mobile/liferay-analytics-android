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
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsClientTest: BaseTest() {

	private val analyticsClient =  AnalyticsClient()
	private lateinit var userId: String

	@Before
	fun setUp() {
		userId = getUserId()
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

	companion object {
		private val ANALYTICS_GATEWAY_DEV =
				"https://ec-dev.liferay.com:8095/api/analyticsgateway/send-analytics-events"
	}
}