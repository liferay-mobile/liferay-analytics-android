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
import java.util.concurrent.TimeUnit

/**
 * @author Igor Matos
 */
class Analytics private constructor(val builder: Builder) {

    fun send(
            @NonNull eventId: String, @NonNull properties: Map<String, String>,
            @NonNull applicationId: String) {

        if (userId == null) {
            return
        }

        val analyticsEventsMessageBuilder = AnalyticsEventsMessage.builder(builder.analyticsKey, userId)

        val eventBuilder = AnalyticsEventsMessage.Event.builder(applicationId, eventId)

        eventBuilder.properties(properties)

        analyticsEventsMessageBuilder.event(eventBuilder.build())

        var flowable = Flowable.fromCallable {
            ANALYTICS_CLIENT_IMPL.sendAnalytics(analyticsEventsMessageBuilder.build())
        }

        flowable.subscribeOn(Schedulers.newThread())
                .subscribe()
    }

    class Builder(@NonNull context: Context?, @NonNull val analyticsKey: String?) {
        val application: Application
        private var flushIntervalInMilliseconds: Long = 200000

        init {
            if (context == null) {
                throw IllegalArgumentException("Context can't be null.")
            }

            if (context.applicationContext == null) {
                throw IllegalArgumentException("AppContext can't be null.")
            }

            if (analyticsKey.isNullOrEmpty()) {
                throw IllegalArgumentException(
                        "Analytics key can't be null or empty.")
            }

            application = context.applicationContext as Application
        }

        fun build(): Analytics {
            return Analytics(this)
        }

        fun setFlushInterval(
                @NonNull flushInterval: Long,
                @NonNull timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Builder {

            if (flushInterval <= 0) {
                throw IllegalArgumentException(
                        "flushInterval can't be less than or equals zero")
            }

            flushIntervalInMilliseconds = timeUnit.toMillis(flushInterval)

            return this
        }

    }

    init {
        analyticsInstance?.let {
            throw IllegalStateException("Your library was already initialized.")
        }

        analyticsInstance = this
    }

    companion object {

        @JvmStatic
        val instance: Analytics
            @Synchronized get() {
                analyticsInstance?.takeUnless {
                    return it
                }

                throw IllegalStateException("You must initialize your library.")

            }

        private val ANALYTICS_CLIENT_IMPL = AnalyticsClientImpl()

        @SuppressLint("StaticFieldLeak")
        private var analyticsInstance: Analytics? = null

        @Nullable
        private val userId: String? = "kAnalyticsDroid"
    }

}