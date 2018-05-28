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

package com.liferay.analytics.android.dao

import com.liferay.analytics.android.BuildConfig
import com.liferay.analytics.android.model.Event
import com.liferay.analytics.android.util.FileStorage
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
class EventsDAOTest {

	private lateinit var eventsDAO: EventsDAO

	@Before
	fun setUp() {
		val filestorage = FileStorage(RuntimeEnvironment.application)
		eventsDAO = EventsDAO(filestorage)

		val eventModel = Event(FIRST_APPLICATION_ID, FIRST_EVENT_ID)

		eventsDAO.addEvents(listOf(eventModel))
	}

	@Test
	fun addAnotherEvent() {
		val lastApplicationId = "lastApplicationId"
		val lastEventId = "lastEventId"

		val eventModel = Event(lastApplicationId, lastEventId)
		eventsDAO.addEvents(listOf(eventModel))

		val events = eventsDAO.getEvents()

		val lastEventModel = events.last()
		Assert.assertEquals(lastEventId, lastEventModel.eventId)
		Assert.assertEquals(lastApplicationId, lastEventModel.applicationId)

		val firstEventModel = events.first()
		Assert.assertEquals(FIRST_EVENT_ID, firstEventModel.eventId)
		Assert.assertEquals(FIRST_APPLICATION_ID, firstEventModel.applicationId)
	}

	@Test
	fun addEvent() {
		val firstEventModel = eventsDAO.getEvents().first()

		Assert.assertEquals(firstEventModel.eventId, FIRST_EVENT_ID)
	}

	@Test
	fun clearEvents() {
		eventsDAO.clear()

		Assert.assertEquals(0, eventsDAO.getEvents().size)
	}

	@Test
	fun replaceEvents() {
		val eventToReplace = Event("applicationId", "eventId")
		eventsDAO.replace(listOf(eventToReplace))

		val events = eventsDAO.getEvents()
		Assert.assertEquals(1, events.size)
		Assert.assertEquals("applicationId", events.first().applicationId)
		Assert.assertEquals("eventId", events.first().eventId)
	}

	companion object {
		private const val FIRST_EVENT_ID = "firstEventId"
		private const val FIRST_APPLICATION_ID = "firstApplicationId"
	}
}