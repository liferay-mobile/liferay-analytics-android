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

package com.liferay.analytics.client.android.model;

import com.google.gson.annotations.SerializedName;

import com.liferay.analytics.model.AnalyticsEventsMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Igor Matos
 * @author Allan Melo
 */
public class AnalyticsEventsMessageModel {

	public AnalyticsEventsMessageModel(
		AnalyticsEventsMessage analyticsEventsMessage) {

		analyticsKey = analyticsEventsMessage.getAnalyticsKey();
		context = analyticsEventsMessage.getContext();
		events = new ArrayList<>();

		for (AnalyticsEventsMessage.Event event :
				analyticsEventsMessage.getEvents()) {

			events.add(new EventModel(event));
		}

		protocolVersion = analyticsEventsMessage.getProtocolVersion();
		userId = analyticsEventsMessage.getUserId();
	}

	public String analyticsKey;
	public Map<String, String> context = new HashMap<>();
	public List<EventModel> events = new ArrayList<>();
	public String protocolVersion;

	@SerializedName(alternate = {"userid"}, value = "userId")
	public String userId;

}