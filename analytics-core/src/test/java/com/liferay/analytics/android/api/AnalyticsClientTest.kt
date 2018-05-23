/*
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

import com.google.gson.reflect.TypeToken
import com.liferay.analytics.android.model.AnalyticsEventsMessage
import com.liferay.analytics.android.model.Event
import com.liferay.analytics.android.util.HTTPClient
import com.liferay.analytics.android.util.JSONParser
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsClientTest {

	private val analyticsCLient = Mockito.spy(AnalyticsClient::class.java)

	private lateinit var userId: String

	@Before
	fun setUp() {
		Mockito.`when`(analyticsCLient.analyticsGatewayHost).thenReturn("192.168.108.90")
		Mockito.`when`(analyticsCLient.analyticsGatewayProtocol).thenReturn("http")
		Mockito.`when`(analyticsCLient.analyticsGatewayPort).thenReturn("8081")
		Mockito.`when`(analyticsCLient.analyticsGatewayPath).thenReturn("/")

		userId = getUserId()
	}

	@Test
	@Throws(Exception::class)
	fun sendAnalytics() {
		var analyticsEventsMessage = AnalyticsEventsMessage("liferay.com", userId).apply {
			context = mapOf("languageId" to "pt_PT",
				"url" to "http://192.168.108.90:8081/")

			protocolVersion = "1.0"

			val event = Event("ApplicationId", "EventId")
			events = mutableListOf(event)
		}

		analyticsCLient.sendAnalytics(analyticsEventsMessage)

		val client = OkHttpClient().newBuilder()
			.readTimeout(300, TimeUnit.SECONDS)
			.writeTimeout(300, TimeUnit.SECONDS)
			.connectTimeout(300, TimeUnit.SECONDS)
			.build()

		val responseBody = HTTPClient.post(CASSANDRA_URL, getQuery(userId), client)

		val list = JSONParser.fromJsonString<ArrayList<AnalyticsEventsMessage>>(
			responseBody, listType())

		Assert.assertTrue(list.isNotEmpty())

		val model = list.first()

		Assert.assertEquals(userId, model.userId)
	}

	private fun getQuery(userId: String?): String {
		return """{ "keyspace": "analytics", "table": "analyticsevent",
                "conditions" : [{"name":"userId","operator":"eq",
                "value": "$userId"}]}"""
	}

	private fun getUserId(): String {
		val currentDate = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())

		return "ANDROID$currentDate"
	}

	private fun listType(): Type {
		return object : TypeToken<ArrayList<AnalyticsEventsMessage>>() {}.type
	}

	companion object {
		private const val CASSANDRA_URL = "http://192.168.108.90:9095/api/query/execute"
	}

}