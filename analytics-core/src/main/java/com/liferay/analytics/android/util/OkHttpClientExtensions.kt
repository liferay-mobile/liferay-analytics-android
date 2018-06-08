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

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.util.Arrays
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * @author Igor Matos
 */

fun OkHttpClient.Builder.trust(certificate: String) : OkHttpClient.Builder {
	val keyStore = CertificateUtil.getKeyStore(certificate)

	val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
	factory.init(keyStore)

	val trustManagers = factory.trustManagers

	if (trustManagers.isEmpty() || trustManagers.first() !is X509TrustManager) {
		throw IllegalStateException(
				"Unexpected default trust managers:" + Arrays.toString(trustManagers))
	}

	val trustManager = trustManagers.first() as X509TrustManager

	val sslContext = SSLContext.getInstance("TLS")
	sslContext.init(null, arrayOf(trustManager), SecureRandom())

	return sslSocketFactory(sslContext.socketFactory, trustManager)
}