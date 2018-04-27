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
import com.liferay.analytics.model.AnalyticsEventsMessage
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

@Config(constants = BuildConfig::class, sdk = intArrayOf(26))
@RunWith(RobolectricTestRunner::class)
class EventsDAOTest {

	private lateinit var eventsDAO: EventsDAO

	@Before
	fun setUp() {
		eventsDAO = EventsDAO(RuntimeEnvironment.application)

		val analyticsEventsMessage = createAnalyticsEventsMessage(FIRST_ELEMENT_ID, FIRST_EVENT_ID)

		eventsDAO.addEvents(listOf(analyticsEventsMessage))
	}

	@Test
	fun addAnalyticsEventsMessageTest() {
		val firstEventFromFirstAnalyticsMessage = eventsDAO.savedEvents.first().events.first()

		Assert.assertEquals(firstEventFromFirstAnalyticsMessage.eventId, FIRST_EVENT_ID)
		Assert.assertEquals(firstEventFromFirstAnalyticsMessage.properties["elementId"], FIRST_ELEMENT_ID)
	}

	@Test
	fun addMoreOneAnalyticsEventsMessageTest() {
		val elementId = "lastElementId"
		val eventId = "lastEventId"

		val analyticsEventsMessage = createAnalyticsEventsMessage(elementId, eventId)
		eventsDAO.addEvents(listOf(analyticsEventsMessage))

		val lastEventFromFirstAnalyticsMessage = eventsDAO.savedEvents.last().events.first()

		Assert.assertEquals(lastEventFromFirstAnalyticsMessage.eventId, eventId)
		Assert.assertEquals(lastEventFromFirstAnalyticsMessage.properties["elementId"], elementId)

		val firstEventFromFirstAnalyticsMessage = eventsDAO.savedEvents.first().events.first()

		Assert.assertEquals(firstEventFromFirstAnalyticsMessage.eventId, FIRST_EVENT_ID)
		Assert.assertEquals(firstEventFromFirstAnalyticsMessage.properties["elementId"], FIRST_ELEMENT_ID)
	}

	fun createAnalyticsEventsMessage(elementId: String, eventId: String): AnalyticsEventsMessage {
		val analyticsEventsMessageBuilder = AnalyticsEventsMessage.builder("analyticsKey", "userId")
		val eventBuilder = AnalyticsEventsMessage.Event.builder("ApplicationId", eventId)

		eventBuilder.property("elementId", elementId)

		analyticsEventsMessageBuilder.event(eventBuilder.build())

		return analyticsEventsMessageBuilder.build()
	}

	companion object {
		private const val FIRST_EVENT_ID = "firstEventId"
		private const val FIRST_ELEMENT_ID = "firstElementId"
	}

}