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
import com.liferay.analytics.android.model.AnalyticsContext
import org.junit.After
import org.junit.Assert
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

	private lateinit var eventsDAO: EventsDAO
    private lateinit var userDAO: UserDAO
    private lateinit var userId: String
	private val dataSourceId = "dataSourceId"

	@Before
	fun setup() {
		Analytics.init(RuntimeEnvironment.application, 240)

		eventsDAO = Analytics.instance.flushProcess.eventsDAO
		userDAO = Analytics.instance.userDAO
		userId = Analytics.instance.flushProcess.getUserId()
	}

	@After
	fun tearDown() {
		Analytics.close()
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
		Assert.assertEquals(dataSourceId, Analytics.instance.dataSourceId)
	}

	@Test
	fun getAnalyticsContext() {
		val analyticsContext = AnalyticsContext(RuntimeEnvironment.application)

		Assert.assertTrue(analyticsContext.userAgent.isNotEmpty())
		Assert.assertTrue(analyticsContext.contentLanguageId.isNotEmpty())
		Assert.assertTrue(analyticsContext.screenHeight > 0)
		Assert.assertTrue(analyticsContext.screenWidth > 0)
	}

}