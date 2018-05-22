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

package com.liferay.analytics.client.android.dao

import com.liferay.analytics.client.android.BuildConfig
import com.liferay.analytics.client.android.model.Identity
import com.liferay.analytics.client.android.model.IdentityContext
import com.liferay.analytics.client.android.util.FileStorage
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * @author Igor Matos
 */
@Config(constants = BuildConfig::class, sdk = [26])
@RunWith(RobolectricTestRunner::class)
class UserDAOTest {

	private lateinit var userDAO: UserDAO

	@Before
	fun setup() {
		val filestorage = FileStorage(RuntimeEnvironment.application)
		userDAO = UserDAO(filestorage)

		userDAO.clearUserId()
		userDAO.clearIdentities()
	}

	@Test
	fun addUserId() {
		val userId = "123456789"
		userDAO.setUserId(userId)
		Assert.assertEquals(userId, userDAO.userId)
	}

	@Test
	fun addIdentityContext() {
		val identityContext1 = IdentityContext("analyticsKey").apply {
			identity = Identity("ned", "ned.ludd@email.com")
		}

		Assert.assertEquals(0, userDAO.userContexts.size)
		userDAO.addIdentityContext(identityContext1)
		Assert.assertEquals(1, userDAO.userContexts.size)

		val userIdentity = userDAO.userContexts.first().identity!!

		Assert.assertEquals("ned", userIdentity.name)
		Assert.assertEquals("ned.ludd@email.com", userIdentity.email)
	}

	@Test
	fun replaceIdentityContexts() {
		val identityContext1 = IdentityContext("analyticsKey")
		val identityContext2 = IdentityContext("analyticsKey")

		Assert.assertEquals(0, userDAO.userContexts.size)
		userDAO.replace(listOf(identityContext1, identityContext2))
		Assert.assertEquals(2, userDAO.userContexts.size)
	}

	@Test
	fun clearSession() {
		userDAO.setUserId("userId1")
		userDAO.clearUserId()

		Assert.assertEquals("", userDAO.userId)
	}

	@Test
	fun setUserId() {
		userDAO.setUserId("userId")
		Assert.assertEquals("userId", userDAO.userId)
	}

	fun clearUserContexts() {
		val identityContext1 = IdentityContext("analyticsKey")
		val identityContext2 = IdentityContext("analyticsKey")

		Assert.assertEquals(0, userDAO.userContexts.size)
		userDAO.replace(listOf(identityContext1, identityContext2))
		Assert.assertEquals(2, userDAO.userContexts.size)

		userDAO.clearIdentities()
		Assert.assertEquals(0, userDAO.userContexts.size)
	}

}