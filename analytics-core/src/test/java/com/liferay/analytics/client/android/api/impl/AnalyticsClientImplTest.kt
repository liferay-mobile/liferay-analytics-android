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

import com.google.gson.reflect.TypeToken
import com.liferay.analytics.client.android.model.AnalyticsEventsMessageModel
import com.liferay.analytics.client.android.util.JSONParser
import com.liferay.analytics.model.AnalyticsEventsMessage
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class AnalyticsClientImplTest {

	private val _analyticsClientImpl = Mockito.spy(AnalyticsClientImpl::class.java)

	private lateinit var _userId: String

	@Before
	fun setUp() {
		Mockito.`when`(_analyticsClientImpl.analyticsGatewayHost)
			.thenReturn("192.168.108.90")

		Mockito.`when`(_analyticsClientImpl.analyticsGatewayProtocol)
			.thenReturn("http")

		Mockito.`when`(_analyticsClientImpl.analyticsGatewayPort)
			.thenReturn("8081")

		Mockito.`when`(_analyticsClientImpl.analyticsGatewayPath)
			.thenReturn("/")

		_userId = _getUserId()
	}

	@Test
	@Throws(Exception::class)
	fun testSendAnalytics() {
		val analyticsEventsMessageBuilder =
			AnalyticsEventsMessage.builder("liferay.com", _userId)
				.contextProperty("languageId", "pt_PT")
				.contextProperty("url", "http://192.168.108.90:8081/")

		val event = AnalyticsEventsMessage.Event.builder("ApplicationId", "View")
			.property("elementId", "banner1")
			.build()

		analyticsEventsMessageBuilder.event(event)
		analyticsEventsMessageBuilder.protocolVersion("1.0")

		_analyticsClientImpl.sendAnalytics(analyticsEventsMessageBuilder.build())

		val body = RequestBody.create(_MEDIA_TYPE, _getQuery(_userId))

		val client = OkHttpClient().newBuilder()
			.readTimeout(300, TimeUnit.SECONDS)
			.writeTimeout(300, TimeUnit.SECONDS)
			.connectTimeout(300, TimeUnit.SECONDS)
			.build()

		val request = Request.Builder()
			.url(_CASSANDRA_URL)
			.post(body)
			.build()

		val response = client.newCall(request).execute() as okhttp3.Response

		val responseBody = response.body()!!.string()

		val listType = object : TypeToken<ArrayList<AnalyticsEventsMessageModel>>() {}.type

		val list = JSONParser.fromJsonString<ArrayList<AnalyticsEventsMessageModel>>(
			responseBody, listType)

		Assert.assertTrue(!list.isEmpty())

		val model = list[0]

		Assert.assertEquals(_userId, model.userId)
	}

	private fun _getQuery(userId: String?): String {
		return """{ "keyspace": "analytics", "table": "analyticsevent",
                "conditions" : [{"name":"userId","operator":"eq",
                "value": "$userId"}]}"""
	}

	private fun _getUserId(): String {
		val currentDate = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())

		return "ANDROID$currentDate"
	}

	companion object {

		private const val _CASSANDRA_URL = "http://192.168.108.90:9095/api/query/execute"

		private val _MEDIA_TYPE = MediaType.parse(
			"application/json; charset=utf-8")
	}

}