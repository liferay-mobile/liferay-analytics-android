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

package com.liferay.analytics.client.android.api.impl;

import com.google.gson.reflect.TypeToken;

import com.liferay.analytics.client.android.model.AnalyticsEventsMessageModel;
import com.liferay.analytics.client.android.util.JSONParser;
import com.liferay.analytics.model.AnalyticsEventsMessage;

import java.lang.reflect.Type;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Igor Matos
 * @author Allan Melo
 */
public class AnalyticsClientImplTest {

	@Before
	public void setUp() {
		Mockito.when(
			_analyticsClientImpl.getAnalyticsGatewayHost()
		).thenReturn(
			"192.168.108.90"
		);

		Mockito.when(
			_analyticsClientImpl.getAnalyticsGatewayProtocol()
		).thenReturn(
			"http"
		);

		Mockito.when(
			_analyticsClientImpl.getAnalyticsGatewayPort()).thenReturn("8081");
		Mockito.when(
			_analyticsClientImpl.getAnalyticsGatewayPath()).thenReturn("/");

		_userId = _getUserId();
	}

	@Test
	public void testSendAnalytics() throws Exception {
		AnalyticsEventsMessage.Builder analyticsEventsMessageBuilder =
			AnalyticsEventsMessage.builder("liferay.com", _userId);

		analyticsEventsMessageBuilder.contextProperty("languageId", "pt_PT");
		analyticsEventsMessageBuilder.contextProperty(
			"url", "http://192.168.108.90:8081/");

		AnalyticsEventsMessage.Event.Builder eventBuilder =
			AnalyticsEventsMessage.Event.builder("ApplicationId", "View");

		eventBuilder.property("elementId", "banner1");

		analyticsEventsMessageBuilder.event(eventBuilder.build());

		analyticsEventsMessageBuilder.protocolVersion("1.0");

		_analyticsClientImpl.sendAnalytics(
			analyticsEventsMessageBuilder.build());

		RequestBody body = RequestBody.create(_MEDIA_TYPE, _getQuery(_userId));

		OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();

		clientBuilder.readTimeout(300, TimeUnit.SECONDS);
		clientBuilder.writeTimeout(300, TimeUnit.SECONDS);
		clientBuilder.connectTimeout(300, TimeUnit.SECONDS);

		OkHttpClient client = clientBuilder.build();

		Request.Builder requestBuilder = new Request.Builder();

		requestBuilder.url(
			_CASSANDRA_URL
		).post(
			body
		);

		Call call = client.newCall(requestBuilder.build());

		Response response = call.execute();

		String responseBody = response.body().string();

		Type listType =
			new TypeToken<ArrayList<AnalyticsEventsMessageModel>>() {
			}.getType();

		ArrayList<AnalyticsEventsMessageModel> list = JSONParser.fromJsonString(
			responseBody, listType);

		Assert.assertTrue(!list.isEmpty());

		AnalyticsEventsMessageModel model = list.get(0);

		Assert.assertEquals(_userId, model.userId);
	}

	private String _getQuery(String userId) {
		return "{\"keyspace\":\"analytics\", \"table\":\"analyticsevent\", " +
			"\"conditions\" : [{\"name\":\"userId\",\"operator\":\"eq\", " +
				"\"value\":\"" + userId + "\"}]}";
	}

	private String _getUserId() {
		String currentDate = new SimpleDateFormat(
			"yyyy.MM.dd.HH.mm.ss").format(new Date());

		return "ANDROID" + currentDate;
	}

	private static final String _CASSANDRA_URL =
		"http://192.168.108.90:9095/api/query/execute";

	private static final MediaType _MEDIA_TYPE = MediaType.parse(
		"application/json; charset=utf-8");

	private final AnalyticsClientImpl _analyticsClientImpl = Mockito.spy(
		AnalyticsClientImpl.class);
	private String _userId;

}