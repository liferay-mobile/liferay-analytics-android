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

package com.liferay.analytics.client.android.model

import com.liferay.analytics.model.IdentityContextMessage

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class IdentityContextMessageModel(
	identityContextMessage: IdentityContextMessage) {

	var analyticsKey: String
	var cookiesEnabled: Boolean = false
	var browserPluginDetails: String?
	var canvasFingerPrint: String?
	var dataSourceIdentifier: String
	var dataSourceIndividualIdentifier: String
	var domain: String
	var httpAcceptHeaders: String?
	var identityFields: Map<String, String> = HashMap()
	var language: String
	var platform: String?
	var protocolVersion: String
	var screenSizeAndColorDepth: String?
	var systemFonts: String?
	var timezone: String?
	var touchSupport: Boolean = false
	var userAgent: String?
	var userId: String?
	var webGLFingerPrint: String?

	init {

		analyticsKey = identityContextMessage.analyticsKey
		browserPluginDetails = identityContextMessage.browserPluginDetails
		canvasFingerPrint = identityContextMessage.canvasFingerPrint
		cookiesEnabled = identityContextMessage.isCookiesEnabled
		dataSourceIdentifier = identityContextMessage.dataSourceIdentifier
		dataSourceIndividualIdentifier = identityContextMessage.dataSourceIndividualIdentifier
		domain = identityContextMessage.domain
		httpAcceptHeaders = identityContextMessage.httpAcceptHeaders
		identityFields = identityContextMessage.identityFields
		language = identityContextMessage.language
		platform = identityContextMessage.platform
		protocolVersion = identityContextMessage.protocolVersion
		screenSizeAndColorDepth = identityContextMessage.screenSizeAndColorDepth
		systemFonts = identityContextMessage.systemFonts
		timezone = identityContextMessage.timezone
		touchSupport = identityContextMessage.isTouchSupport
		userAgent = identityContextMessage.userAgent
		userId = identityContextMessage.userId
		webGLFingerPrint = identityContextMessage.webGLFingerPrint
	}

}