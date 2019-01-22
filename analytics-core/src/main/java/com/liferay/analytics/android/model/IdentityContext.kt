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

import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

/**
 * @author Igor Matos
 * @author Allan Melo
 */
internal data class IdentityContext(var dataSourceId: String): KoinComponent {
	@delegate:Transient private val analyticsContext: AnalyticsContext by inject()

	var identity: Identity? = null
	val language: String = Locale.getDefault().toString()
	val platform = "Android"
	val timezone: String = TimeZone.getDefault().displayName
	var userAgent: String
	var userId: String

	init {
		userAgent = analyticsContext.userAgent
		userId = createUserId()
	}

	fun createUserId(): String {
		val uuid = UUID.randomUUID().toString()

		return uuid.take(36)
	}
}