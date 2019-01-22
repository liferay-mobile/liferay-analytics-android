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

package com.liferay.analytics.android.model

import com.google.gson.annotations.SerializedName
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.HashMap

/**
 * @author Igor Matos
 * @author Allan Melo
 */
internal data class AnalyticsEvents(
		var dataSourceId: String,
		@SerializedName(alternate = ["userid"], value = "userId") var userId: String): KoinComponent {

	val context: AnalyticsContext = inject<AnalyticsContext>().value
	var events: List<Event> = listOf()
	var protocolVersion: String? = null
}