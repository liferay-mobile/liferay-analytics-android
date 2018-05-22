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

import java.util.Locale
import java.util.UUID

/**
 * @author Igor Matos
 * @author Allan Melo
 */
data class IdentityContext(var analyticsKey: String) {

	var userId: String
	var identity: Identity? = null
	var timezone: String? = null
	val language: String = Locale.getDefault().toString()

	init {
		userId = createUserId()
	}

	fun createUserId(): String {
		val uuid = UUID.randomUUID().toString()

		return uuid.take(20)
	}

	companion object {
		const val platform: String = "Android"
	}

}