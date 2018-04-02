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

package com.liferay.analytics.client.android.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import com.liferay.analytics.model.AnalyticsEventsMessage;
import com.liferay.analytics.model.IdentityContextMessage;

import java.lang.reflect.Type;

/**
 * @author Igor Matos
 * @author Allan Melo
 */
public class JSONParser {

	public static <T> T fromJsonString(String json, Type type)
		throws JsonSyntaxException {

		return gson().fromJson(json, type);
	}

	public static String toJSON(Object element) {
		return gson().toJson(element);
	}

	protected static synchronized Gson gson() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.setDateFormat(_DATE_FORMAT);

		gsonBuilder.registerTypeAdapter(
			AnalyticsEventsMessage.class,
			new AnalyticsEventsMessageSerializer());

		gsonBuilder.registerTypeAdapter(
			IdentityContextMessage.class,
			new IdentityContextMessageSerializer());

		return gsonBuilder.create();
	}

	private static final String _DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

}