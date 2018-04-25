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

package com.liferay.analytics.client.android.api.impl

import com.liferay.analytics.model.IdentityContextMessage

import org.junit.Assert
import org.junit.Test

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class IdentityClientImplTest {

	@Test
	@Throws(Exception::class)
	fun testGetUserId() {
		val identityContextMessageBuilder =
			IdentityContextMessage.builder("liferay.com")

		identityContextMessageBuilder.dataSourceIdentifier("Liferay")
		identityContextMessageBuilder.dataSourceIndividualIdentifier("12345")
		identityContextMessageBuilder.domain("liferay.com")
		identityContextMessageBuilder.language("en-US")
		identityContextMessageBuilder.protocolVersion("1.0")

		identityContextMessageBuilder.identityFieldsProperty("email", "joe.blogs@liferay.com")
		identityContextMessageBuilder.identityFieldsProperty("name", "Joe Bloggs")

		val identityClientImpl = IdentityClientImpl()

		val userId = identityClientImpl.getUserId(identityContextMessageBuilder.build())

		Assert.assertTrue(userId.isNotEmpty())
	}

}