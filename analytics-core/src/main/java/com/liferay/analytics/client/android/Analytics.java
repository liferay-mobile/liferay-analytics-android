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

package com.liferay.analytics.client.android;

import android.content.Context;

import com.liferay.analytics.client.android.api.impl.AnalyticsClientImpl;
import com.liferay.analytics.model.AnalyticsEventsMessage;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author Igor Matos
 */
public class Analytics {

	public static synchronized Analytics getInstance() {
		if (_analyticsInstance == null) {
			throw new IllegalStateException(
				"You must initialize your library.");
		}

		return _analyticsInstance;
	}

	public void send(
		@NonNull String eventId, @NonNull Map<String, String> properties,
		@NonNull String applicationId) {

		if (_userId == null) {
			return;
		}

		final AnalyticsEventsMessage.Builder analyticsEventsMessageBuilder =
			AnalyticsEventsMessage.builder(_analyticsKey, _userId);

		AnalyticsEventsMessage.Event.Builder eventBuilder =
			AnalyticsEventsMessage.Event.builder(applicationId, eventId);

		eventBuilder.properties(properties);

		analyticsEventsMessageBuilder.event(eventBuilder.build());

		Flowable flowable = Flowable.fromCallable(new Callable() {

			@Override
			public Object call() throws Exception {
				_ANALYTICS_CLIENT_IMPL.sendAnalytics(
					analyticsEventsMessageBuilder.build());

				return Observable.empty();
			}

		});

		flowable = flowable.subscribeOn(Schedulers.newThread());

		flowable.subscribe();
	}

	public static class Builder {

		public Builder(@NonNull Context context, @NonNull String analyticsKey) {
			if (context == null) {
				throw new IllegalArgumentException("Context can't be null.");
			}

			if (context.getApplicationContext() == null) {
				throw new IllegalArgumentException("AppContext can't be null.");
			}

			if ((analyticsKey == null) || (analyticsKey.isEmpty())) {
				throw new IllegalArgumentException(
					"Analytics key can't be null or empty.");
			}

			_context = context.getApplicationContext();
			_analyticsKey = analyticsKey;
		}

		public Analytics build() {
			return new Analytics(
				_context, _analyticsKey, _flushIntervalInMilliseconds);
		}

		public Builder setFlushInterval(
			@NonNull long flushInterval, @NonNull TimeUnit timeUnit) {

			if (timeUnit == null) {
				throw new IllegalArgumentException("TimeUnit can't be null.");
			}

			if (flushInterval <= 0) {
				throw new IllegalArgumentException(
					"flushInterval can't be less than zero.");
			}

			_flushIntervalInMilliseconds = timeUnit.toMillis(flushInterval);

			return this;
		}

		private String _analyticsKey;
		private Context _context;
		private long _flushIntervalInMilliseconds = 200000;

	}

	private Analytics(
		Context context, String analyticsKey,
		long flushIntervalInMilliseconds) {

		if (_analyticsInstance != null) {
			throw new IllegalStateException(
				"Your library was already initialized.");
		}

		_analyticsKey = analyticsKey;
		_flushIntervalInMilliseconds = flushIntervalInMilliseconds;
		_context = context;
		_analyticsInstance = this;
	}

	private static final AnalyticsClientImpl _ANALYTICS_CLIENT_IMPL =
		new AnalyticsClientImpl();

	private static Analytics _analyticsInstance;

	@Nullable
	private static String _userId = "analyticsDroid";

	private final String _analyticsKey;
	private final Context _context;
	private final long _flushIntervalInMilliseconds;

}