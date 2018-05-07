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

package com.liferay.analytics.client.android.util

import com.liferay.analytics.client.android.Analytics
import com.liferay.analytics.client.android.BuildConfig
import com.liferay.analytics.client.android.dao.EventsDAO
import com.liferay.analytics.client.android.model.Event
import junit.framework.Assert
import org.junit.After
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
class FlushProcessTest {

	private lateinit var flushProcess: FlushProcess
	private lateinit var eventsDAO: EventsDAO


	@Before
	fun setup() {
		Analytics.init(RuntimeEnvironment.application, "analyticsKey")

		flushProcess = Analytics.flushProcess
		eventsDAO = flushProcess.eventsDAO

		eventsDAO.clear()
	}

	@After
	fun tearDown() {
		Analytics.instance = null
	}

	@Test
	fun inProgressTest() {
		val event1 = Event("applicationId1", "eventId1")
		val event2 = Event("applicationId2", "eventId2")

		Assert.assertEquals(0, eventsDAO.events.size)
		flushProcess.addEvent(event1)

		Assert.assertEquals(1, eventsDAO.events.size)
		Assert.assertEquals(0, flushProcess.eventsQueue.size)

		flushProcess.isInProgress = true
		flushProcess.addEvent(event2)

		Assert.assertEquals(1, eventsDAO.events.size)
		Assert.assertEquals(1, flushProcess.eventsQueue.size)
	}

	@Test
	fun getEventsToSave() {
		var events = flushProcess.getRemainingEvents()
		Assert.assertEquals(0, events.count())

		createEvents(size = 1)
		events = flushProcess.getRemainingEvents()
		Assert.assertEquals(0, events.count())

		createEvents(size = 101)
		events = flushProcess.getRemainingEvents()
		Assert.assertEquals(1, events.count())
		Assert.assertEquals("event101", events.first().eventId)

		createEvents(size = 250)
		events = flushProcess.getRemainingEvents()
		Assert.assertEquals(150, events.count())
		Assert.assertEquals("event101", events.first().eventId)
		Assert.assertEquals("event250", events.last().eventId)
	}


	@Test
	fun getEventsToSend() {
		var events = flushProcess.getEventsToSend()
		Assert.assertEquals(0, events.count())

		createEvents(size = 1)
		events = flushProcess.getEventsToSend()
		Assert.assertEquals(1, events.count())

		createEvents(size = 101)
		events = flushProcess.getEventsToSend()
		Assert.assertEquals(100, events.count())
		Assert.assertEquals("event1", events.first().eventId)
		Assert.assertEquals("event100", events.last().eventId)

		createEvents(size = 250)
		events = flushProcess.getEventsToSend()
		Assert.assertEquals(100, events.count())
		Assert.assertEquals("event1", events.first().eventId)
		Assert.assertEquals("event100", events.last().eventId)
	}

	fun createEvents(size: Int) {
		var events: MutableList<Event> = mutableListOf()

		for (i in 1..size) {
			events.add(Event("appId$i", "event$i"))
		}

		eventsDAO.replace(events)
	}


}