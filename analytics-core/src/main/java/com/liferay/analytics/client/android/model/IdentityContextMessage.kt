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

/**
 * @author Igor Matos
 * @author Allan Melo
 */
data class IdentityContextMessage(var analyticsKey: String) {

	var identityFields: MutableMap<String, String> = mutableMapOf()
	var language: String? = null
	var platform: String? = null
	var protocolVersion: String? = null
	var screenSizeAndColorDepth: String? = null
	var systemFonts: String? = null
	var timezone: String? = null
	var touchSupport = false
	var userId: String? = null

}