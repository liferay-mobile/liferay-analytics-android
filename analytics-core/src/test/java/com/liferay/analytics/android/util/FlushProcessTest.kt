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

import android.content.pm.PackageManager
import com.liferay.analytics.android.Analytics
import com.liferay.analytics.android.BuildConfig
import com.liferay.analytics.android.dao.EventsDAO
import com.liferay.analytics.android.dao.UserDAO
import com.liferay.analytics.android.model.Event
import com.liferay.analytics.android.model.IdentityContext
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.koin.standalone.StandAloneContext.closeKoin
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * @author Igor Matos
 */
@Config(constants = BuildConfig::class, sdk = [26])
@RunWith(RobolectricTestRunner::class)
class FlushProcessTest {

	private lateinit var eventsDAO: EventsDAO
	private lateinit var flushProcess: FlushProcess
	private lateinit var server: MockWebServer
	private lateinit var userDAO: UserDAO

	@Before
	fun setup() {
		server = MockWebServer()
		server.enqueue(MockResponse().setBody("{}").setResponseCode(200))
		val mockedURL = server.url("/")

		mockEndpointUrl(mockedURL)

		Analytics.init(RuntimeEnvironment.application)

		flushProcess = Analytics.instance.flushProcess
		eventsDAO = flushProcess.eventsDAO
		userDAO = flushProcess.userDAO
	}

	@After
	fun tearDown() {
		eventsDAO.clear()
		userDAO.clearIdentities()
		userDAO.clearUserId()
		Analytics.close()
		closeKoin()
		server.shutdown()
	}

	@Test
	fun getUserId() {
		val userId = "123456789"
		userDAO.setUserId(userId)
		Assert.assertEquals(userId, flushProcess.getUserId())
	}

	@Test
	fun saveEventsQueue() {
		val event1 = Event("applicationId1", "eventId1")

		flushProcess.isInProgress = true
		flushProcess.addEvent(event1)

		Assert.assertEquals(0, eventsDAO.getEvents().size)
		Assert.assertEquals(1, flushProcess.eventsQueue.size)

		flushProcess.saveEventsQueue()
		Assert.assertEquals(1, eventsDAO.getEvents().size)
		Assert.assertEquals(0, flushProcess.eventsQueue.size)
	}

	@Test
	fun sendIdentities() {
		val defaultUserContext = IdentityContext(Analytics.instance.dataSourceId)

		userDAO.addIdentityContext(defaultUserContext)
		Assert.assertEquals(1, userDAO.getUserContexts().size)
		flushProcess.sendIdentities()
		Assert.assertEquals(0, userDAO.getUserContexts().size)
	}

	@Test
	fun shouldAddEventsInQueue() {
		val event1 = Event("applicationId1", "eventId1")
		val event2 = Event("applicationId2", "eventId2")

		Assert.assertEquals(0, eventsDAO.getEvents().size)
		flushProcess.addEvent(event1)

		Assert.assertEquals(1, eventsDAO.getEvents().size)
		Assert.assertEquals(0, flushProcess.eventsQueue.size)

		flushProcess.isInProgress = true
		flushProcess.addEvent(event2)

		Assert.assertEquals(1, eventsDAO.getEvents().size)
		Assert.assertEquals(1, flushProcess.eventsQueue.size)
	}

	private fun mockEndpointUrl(mockedURL: HttpUrl) {
		val context = RuntimeEnvironment.application
		val applicationInfo =
			context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)

		applicationInfo.metaData.putString("com.liferay.analytics.EndpointUrl", mockedURL.toString())
	}

}