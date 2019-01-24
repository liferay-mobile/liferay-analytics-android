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

package com.liferay.analytics.android.util

import com.liferay.analytics.android.BuildConfig
import com.liferay.analytics.android.util.extensions.trust
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Igor Matos
 */
internal class HttpClient {
	companion object {

		@Throws(IOException::class)
		fun post(url: String, config: HttpClientConfig): String {

			val body = RequestBody.create(MEDIA_TYPE, config.body)

			val requestBuilder = Request.Builder()
				.url(url)
				.post(body)

			config.userAgent?.also {
				requestBuilder.addHeader("User-Agent", it)
			}

			val request = requestBuilder.build()

			val builder = OkHttpClient.Builder()
				.readTimeout(config.timeout, TimeUnit.SECONDS)
				.writeTimeout(config.timeout, TimeUnit.SECONDS)
				.connectTimeout(config.timeout, TimeUnit.SECONDS)

			if (!config.certificate.isEmpty()) {
				builder.trust(config.certificate)
			}

			if (BuildConfig.DEBUG) {
				builder.addInterceptor(LoggingInterceptor())
			}

			val client = builder.build()
			val response = client.newCall(request).execute()

			return response.body()?.string() ?: ""
		}

		private val MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
	}
}

internal class HttpClientConfig {
	var body: String = "{}"
	var certificate: String = ""
	var timeout: Long = 30
	var userAgent: String? = null
}