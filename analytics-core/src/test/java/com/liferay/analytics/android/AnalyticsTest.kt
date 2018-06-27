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

package com.liferay.analytics.android

import com.liferay.analytics.android.dao.EventsDAO
import com.liferay.analytics.android.dao.UserDAO
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * @author Igor Matos
 */
@Config(constants = BuildConfig::class, sdk = [26])
@RunWith(RobolectricTestRunner::class)
class AnalyticsTest {

	private lateinit var analytics: Analytics
	private lateinit var userDAO: UserDAO
	private lateinit var userId: String
	private lateinit var eventsDAO: EventsDAO
	private val analyticsKey = "analyticsKey"

	@Before
	fun setup() {
		Analytics.configure(RuntimeEnvironment.application, analyticsKey, 240)
		analytics = Analytics.instance!!

		userId = analytics.flushProcess.getUserId()
		eventsDAO = analytics.flushProcess.eventsDAO
		userDAO = analytics.userDAO
	}

	@After
	fun tearDown() {
		Analytics.instance = null
		closeKoin()
	}

	@Test
	fun setIdentity() {
		Analytics.setIdentity("email@email.com", "name")

		val identity = userDAO.getUserContexts().last().identity!!

		Assert.assertEquals("email@email.com", identity.email)
		Assert.assertEquals("name", identity.name)
	}

	@Test
	fun clearIdentity() {
		Analytics.setIdentity("email@email.com", "name")

		Assert.assertNotSame("", userId)
		Analytics.clearSession()

		val userId = userDAO.getUserId()

		Assert.assertEquals("", userId)
	}

	@Test
	fun sendCustomEventWithProperties() {
		Analytics.send("customEventId",
			"customApplicationId",
			mapOf("customProperty" to "customValue"))

		val event = eventsDAO.getEvents()[userId]!!.first()

		Assert.assertEquals("customEventId", event.eventId)
		Assert.assertEquals("customApplicationId", event.applicationId)
		Assert.assertEquals("customValue", event.properties["customProperty"])
	}

	@Test
	fun getInstance() {
		Assert.assertEquals(analyticsKey, Analytics.instance!!.analyticsKey)
	}

}