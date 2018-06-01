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

package com.liferay.analytics.android.api

import com.liferay.analytics.android.model.Identity
import com.liferay.analytics.android.model.IdentityContext
import org.junit.Assert
import org.junit.Test
import java.io.IOException

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class IdentityClientTest {

	@Test
	fun sendUserId() {
		val identityContext = IdentityContext("liferay.com").apply {
			identity = Identity("Ned Ludd", "ned.ludd@email.com")
		}

		val identityClient = IdentityClient()

		try {
			identityClient.send(identityContext)
		}
		catch (e: IOException) {
			Assert.fail()
		}
	}

	@Test
	fun createUserId() {
		val identityContext = IdentityContext("liferay.com")
		val userId = identityContext.userId

		Assert.assertEquals(userId.length, 20)
	}
}