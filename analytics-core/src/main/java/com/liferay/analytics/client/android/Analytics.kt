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

package com.liferay.analytics.client.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.liferay.analytics.client.android.api.impl.AnalyticsClientImpl
import com.liferay.analytics.model.AnalyticsEventsMessage
import io.reactivex.Flowable
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.schedulers.Schedulers

/**
 * @author Igor Matos
 */
class Analytics private constructor(val application: Application, private val analyticsKey: String,
	private val flushIntervalInMilliseconds: Long) {

	init {
		analyticsInstance?.let {
			throw IllegalStateException("Your library was already initialized.")
		}

		analyticsInstance = this
	}

	companion object {

		@JvmOverloads
		@JvmStatic
		fun init(@NonNull context: Context, @NonNull analyticsKey: String,
			flushIntervalInMilliseconds: Long = FLUSH_INTERVAL_DEFAULT) {

			if (context == null) {
				throw IllegalArgumentException("Context can't be null.")
			}

			if (context.applicationContext == null) {
				throw IllegalArgumentException("AppContext can't be null.")
			}

			if (analyticsKey.isNullOrEmpty() || analyticsKey.isBlank()) {
				throw IllegalArgumentException("Analytics key can't be null or empty.")
			}

			if (flushIntervalInMilliseconds <= 0) {
				throw IllegalArgumentException("flushInterval can't be less than or equals zero.")
			}

			val application = context.applicationContext as Application
			Analytics(application, analyticsKey, flushIntervalInMilliseconds)
		}

		@JvmOverloads
		@JvmStatic
		fun send(@NonNull eventId: String, @NonNull applicationId: String,
			@NonNull properties: Map<String, String> = hashMapOf()) {

			if (userId == null) {
				return
			}

			if (eventId.isNullOrEmpty() || eventId.isBlank()) {
				throw IllegalArgumentException("EventId can't be null or empty.")
			}

			if (applicationId.isNullOrEmpty() || applicationId.isBlank()) {
				throw IllegalArgumentException("ApplicationId can't be null or empty.")
			}

			if (properties == null) {
				throw IllegalArgumentException("Properties can't be null.")
			}

			val analyticsEventsMessageBuilder = AnalyticsEventsMessage.builder(
				instance.analyticsKey, userId)

			val eventBuilder = AnalyticsEventsMessage.Event.builder(applicationId, eventId)

			eventBuilder.properties(properties)

			analyticsEventsMessageBuilder.event(eventBuilder.build())

			val flowable = Flowable.fromCallable({
				ANALYTICS_CLIENT_IMPL.sendAnalytics(analyticsEventsMessageBuilder.build())
			})

			flowable.subscribeOn(Schedulers.newThread())
				.subscribe()
		}

		@JvmStatic
		private val instance: Analytics
			@Synchronized get() {
				analyticsInstance?.let {
					return it
				}

				throw IllegalStateException("You must initialize your library")
			}

		private val ANALYTICS_CLIENT_IMPL = AnalyticsClientImpl()

		@SuppressLint("StaticFieldLeak")
		private var analyticsInstance: Analytics? = null

		@Nullable
		private val userId: String? = "kAnalyticsDroid"

		private const val FLUSH_INTERVAL_DEFAULT: Long = 2000
	}

}