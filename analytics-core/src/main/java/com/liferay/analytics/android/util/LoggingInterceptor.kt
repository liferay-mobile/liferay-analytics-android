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

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.standalone.KoinComponent
import java.io.IOException

/**
 * @author Victor Oliveira
 */

internal class LoggingInterceptor : Interceptor, KoinComponent {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val startTimeRequest = System.nanoTime()
        println("Sending request ${request.url()}" +
            "\n${request.headers()}")

        val response = chain.proceed(request)

        val endTimeRequest = System.nanoTime()
        println("Received response for ${response.request().url()}" +
            " in ${(endTimeRequest - startTimeRequest) / 1e6}ms" +
            "\n${response.headers()}")

        return response
    }
}