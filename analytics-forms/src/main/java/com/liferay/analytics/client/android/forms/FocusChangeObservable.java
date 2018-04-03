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

package com.liferay.analytics.client.android.forms;

import android.util.Pair;

import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import java.sql.Timestamp;

/**
 * @author Igor Matos
 */
public class FocusChangeObservable extends Observable<Pair<Boolean, Long>> {

	public FocusChangeObservable(View view) {
		_view = view;
	}

	@Override
	protected void subscribeActual(
		Observer<? super Pair<Boolean, Long>> observer) {

		FocusChangeObservable.Listener listener =
			new FocusChangeObservable.Listener(_view, observer);

		observer.onSubscribe(listener);
		_view.setOnFocusChangeListener(listener);
	}

	protected static final class Listener extends MainThreadDisposable
		implements View.OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			Timestamp systemTimestamp = new Timestamp(
				System.currentTimeMillis());

			long currentTimestamp = systemTimestamp.getTime();

			long focusedDuration =
				hasFocus ? 0 : currentTimestamp - _lastTimestamp;

			if (!isDisposed()) {
				_observer.onNext(new Pair<>(hasFocus, focusedDuration));
			}

			_lastTimestamp = currentTimestamp;
		}

		protected Listener(
			View view, Observer<? super Pair<Boolean, Long>> observer) {

			_view = view;
			_observer = observer;
		}

		@Override
		protected void onDispose() {
			_view.setOnFocusChangeListener(null);
		}

		private long _lastTimestamp = Long.MIN_VALUE;
		private final Observer<? super Pair<Boolean, Long>> _observer;
		private final View _view;

	}

	private final View _view;

}