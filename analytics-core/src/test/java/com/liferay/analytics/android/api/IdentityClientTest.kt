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

import com.liferay.analytics.android.BaseTest
import com.liferay.analytics.android.model.Identity
import com.liferay.analytics.android.model.IdentityContext
import org.junit.Assert
import org.junit.Test
import java.io.IOException

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class IdentityClientTest: BaseTest() {

	@Test
	fun sendUserId() {
		val identityContext = IdentityContext("analyticsKey").apply {
			identity = Identity("Ned Ludd", "ned.ludd@email.com")
		}

		try {
			IdentityClient().send(identityContext)
		}
		catch (e: IOException) {
			Assert.fail(e.localizedMessage)
		}
	}

	@Test
	fun createUserId() {
		val identityContext = IdentityContext("analyticsKey")
		val userId = identityContext.userId

		Assert.assertEquals(userId.length, 20)
	}

	companion object {
		private val IDENTITY_GATEWAY_DEV =
				"https://ec-dev.liferay.com:8095/api/identitycontextgateway/send-identity-context"
	}
}