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

import android.util.Log
import java.security.KeyStore
import java.security.cert.CertificateFactory

/**
 * @author Igor Matos
 */
class CertificateUtil {

	companion object {

		fun getKeyStore(fileName: String): KeyStore? {
			try {
				val certificateFactory = CertificateFactory.getInstance(CERTIFICATION_TYPE)
				val caInput = javaClass.classLoader.getResourceAsStream(fileName)

				caInput.use {
					val ca = certificateFactory.generateCertificate(it)

					val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
					keyStore.load(null, null)
					keyStore.setCertificateEntry("ca", ca)

					return keyStore
				}
			}
			catch (e: Exception) {
				Log.e("LIFERAY-ANALYTICS",
						"Error during getting keystore ${e.localizedMessage}")
			}

			return null
		}

		private const val CERTIFICATION_TYPE = "X.509"
	}

}