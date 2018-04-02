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

import com.liferay.analytics.client.AnalyticsClient;
import com.liferay.analytics.client.android.util.JSONParser;
import com.liferay.analytics.model.AnalyticsEventsMessage;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Igor Matos
 * @author Allan Melo
 */
public class AnalyticsClientImpl implements AnalyticsClient {

	@Override
	public String sendAnalytics(AnalyticsEventsMessage analyticsEventsMessage)
		throws Exception {

		String json = JSONParser.toJSON(analyticsEventsMessage);

		String analyticsPath = String.format(
			"%s://%s:%s%s", getAnalyticsGatewayProtocol(),
			getAnalyticsGatewayHost(), getAnalyticsGatewayPort(),
			getAnalyticsGatewayPath());

		return _post(analyticsPath, json);
	}

	protected String getAnalyticsGatewayHost() {
		return _ANALYTICS_GATEWAY_HOST;
	}

	protected String getAnalyticsGatewayPath() {
		return _ANALYTICS_GATEWAY_PATH;
	}

	protected String getAnalyticsGatewayPort() {
		return _ANALYTICS_GATEWAY_PORT;
	}

	protected String getAnalyticsGatewayProtocol() {
		return _ANALYTICS_GATEWAY_PROTOCOL;
	}

	private String _post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(_MEDIA_TYPE, json);

		Request.Builder requestBuilder = new Request.Builder();

		requestBuilder.url(
			url
		).post(
			body
		);

		Call call = _client.newCall(requestBuilder.build());

		Response response = call.execute();

		return response.body().string();
	}

	private static final String _ANALYTICS_GATEWAY_HOST = "ec-dev.liferay.com";

	private static final String _ANALYTICS_GATEWAY_PATH =
		"/api/analyticsgateway/send-analytics-events";

	private static final String _ANALYTICS_GATEWAY_PORT = "8095";

	private static final String _ANALYTICS_GATEWAY_PROTOCOL = "https";

	private static final MediaType _MEDIA_TYPE = MediaType.parse(
		"application/json; charset=utf-8");

	private final OkHttpClient _client = new OkHttpClient();

}