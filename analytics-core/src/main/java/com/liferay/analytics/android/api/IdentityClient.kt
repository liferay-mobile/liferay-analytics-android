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

package com.liferay.analytics.android.api

import android.content.Context
import com.liferay.analytics.android.R
import com.liferay.analytics.android.model.IdentityContext
import com.liferay.analytics.android.util.HttpClient
import com.liferay.analytics.android.util.HttpClientConfig
import com.liferay.analytics.android.util.JSONParser
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.IOException

/**
 * @author Igor Matos
 * @author Allan Melo
 */
internal class IdentityClient: KoinComponent {
	private val context : Context by inject()

	@Throws(IOException::class)
	fun send(endpointURL: String, identityContext: IdentityContext): String {
		val config = HttpClientConfig().apply {
			body = JSONParser.toJSON(identityContext)
			certificate = context.getString(R.string.certificate)
			userAgent = identityContext.userAgent
		}

		return HttpClient.post("$endpointURL/identity", config)
	}
}