/*
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

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

/**
 * @author Igor Matos
 */
internal class HTTPClient {

	companion object {

		@Throws(IOException::class)
		fun post(url: String, json: String, client: OkHttpClient = OkHttpClient()): String {
			val body = RequestBody.create(MEDIA_TYPE, json)

			val request = Request.Builder()
				.url(url)
				.post(body)
				.build()

			val response = client.newCall(request).execute()

			response.body()?.let {
				return it.string()
			}

			return ""
		}

		private val MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")

	}
}