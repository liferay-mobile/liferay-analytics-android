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
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class IdentityClientTest: BaseTest() {

	@Test
	fun getIdentityContext() {
		val identityContext = IdentityContext("dataSourceId")
		val userId = identityContext.userId
		val userAgent = identityContext.userAgent

		Assert.assertEquals(userId.length, 36)
		Assert.assertTrue(userAgent.isNotEmpty())
	}

	@Test
	@Throws(Exception::class)
	fun sendIdentity() {
		val server = MockWebServer()
		server.enqueue(MockResponse().setBody("{}").setResponseCode(200))
		val mockedURL = server.url("/")

		val identityContext = IdentityContext("dataSourceId").apply {
			identity = Identity("Ned Ludd", "ned.ludd@email.com")
		}

		IdentityClient().send(mockedURL.toString(), identityContext)

		val request = server.takeRequest()
		val requestBody = JSONObject(request.body.readUtf8())

		val identityJSON = requestBody.getJSONObject("identity")
		Assert.assertEquals("Ned Ludd", identityJSON.getString("name"))
		Assert.assertEquals("ned.ludd@email.com", identityJSON.getString("email"))

		Assert.assertTrue(requestBody.has("language"))
		Assert.assertTrue(requestBody.has("timezone"))
		Assert.assertTrue(requestBody.has("userAgent"))

		Assert.assertEquals("Android", requestBody.getString("platform"))
		Assert.assertEquals("dataSourceId", requestBody.getString("dataSourceId"))
		Assert.assertEquals(identityContext.userId, requestBody.getString("userId"))

		server.shutdown()
	}

}