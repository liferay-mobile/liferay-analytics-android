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

import com.liferay.analytics.client.IdentityClient;
import com.liferay.analytics.client.android.util.JSONParser;
import com.liferay.analytics.model.IdentityContextMessage;

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
public class IdentityClientImpl implements IdentityClient {

	@Override
	public String getUserId(IdentityContextMessage identityContextMessage)
		throws Exception {

		String json = JSONParser.toJSON(identityContextMessage);

		String identityPath = String.format(
			"%s://%s:%s/%s%s", getIdentityGatewayProtocol(),
			getIdentityGatewayHost(), getIdentityGatewayPort(),
			identityContextMessage.getAnalyticsKey(), getIdentityGatewayPath());

		return _post(identityPath, json);
	}

	protected String getIdentityGatewayHost() {
		return _IDENTITY_GATEWAY_HOST;
	}

	protected String getIdentityGatewayPath() {
		return _IDENTITY_GATEWAY_PATH;
	}

	protected String getIdentityGatewayPort() {
		return _IDENTITY_GATEWAY_PORT;
	}

	protected String getIdentityGatewayProtocol() {
		return _IDENTITY_GATEWAY_PROTOCOL;
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

	private static final String _IDENTITY_GATEWAY_HOST =
		"contacts-prod.liferay.com";

	private static final String _IDENTITY_GATEWAY_PATH = "/identity";

	private static final String _IDENTITY_GATEWAY_PORT = "443";

	private static final String _IDENTITY_GATEWAY_PROTOCOL = "https";

	private static final MediaType _MEDIA_TYPE = MediaType.parse(
		"application/json; charset=utf-8");

	private final OkHttpClient _client = new OkHttpClient();

}