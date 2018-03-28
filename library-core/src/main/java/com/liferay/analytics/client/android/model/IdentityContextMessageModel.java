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

import com.liferay.analytics.model.IdentityContextMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Matos
 * @author Allan Melo
 */
public class IdentityContextMessageModel {

	public IdentityContextMessageModel(
		IdentityContextMessage identityContextMessage) {

		analyticsKey = identityContextMessage.getAnalyticsKey();
		browserPluginDetails = identityContextMessage.getBrowserPluginDetails();
		canvasFingerPrint = identityContextMessage.getCanvasFingerPrint();
		cookiesEnabled = identityContextMessage.isCookiesEnabled();
		dataSourceIdentifier = identityContextMessage.getDataSourceIdentifier();
		dataSourceIndividualIdentifier =
			identityContextMessage.getDataSourceIndividualIdentifier();
		domain = identityContextMessage.getDomain();
		httpAcceptHeaders = identityContextMessage.getHttpAcceptHeaders();
		identityFields = identityContextMessage.getIdentityFields();
		language = identityContextMessage.getLanguage();
		platform = identityContextMessage.getPlatform();
		protocolVersion = identityContextMessage.getProtocolVersion();
		screenSizeAndColorDepth =
			identityContextMessage.getScreenSizeAndColorDepth();
		systemFonts = identityContextMessage.getSystemFonts();
		timezone = identityContextMessage.getTimezone();
		touchSupport = identityContextMessage.isTouchSupport();
		userAgent = identityContextMessage.getUserAgent();
		userId = identityContextMessage.getUserId();
		webGLFingerPrint = identityContextMessage.getWebGLFingerPrint();
	}

	public String analyticsKey;
	public String browserPluginDetails;
	public String canvasFingerPrint;
	public boolean cookiesEnabled;
	public String dataSourceIdentifier;
	public String dataSourceIndividualIdentifier;
	public String domain;
	public String httpAcceptHeaders;
	public Map<String, String> identityFields = new HashMap<>();
	public String language;
	public String platform;
	public String protocolVersion;
	public String screenSizeAndColorDepth;
	public String systemFonts;
	public String timezone;
	public boolean touchSupport;
	public String userAgent;
	public String userId;
	public String webGLFingerPrint;

}