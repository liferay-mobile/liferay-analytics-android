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

package com.liferay.analytics.client.android.forms

import android.util.Pair

import android.view.View

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import java.sql.Timestamp

/**
 * @author Igor Matos
 */
class FocusChangeObservable(private val _view: View) : Observable<Pair<Boolean, Long>>() {

	override fun subscribeActual(observer: Observer<in Pair<Boolean, Long>>) {

		val listener = FocusChangeObservable.Listener(_view, observer)

		observer.onSubscribe(listener)
		_view.onFocusChangeListener = listener
	}

	protected class Listener(
		private val _view: View, private val _observer: Observer<in Pair<Boolean, Long>>)
		: MainThreadDisposable(), View.OnFocusChangeListener {

		private var _lastTimestamp = java.lang.Long.MIN_VALUE

		override fun onFocusChange(v: View, hasFocus: Boolean) {
			val systemTimestamp = Timestamp(
				System.currentTimeMillis())

			val currentTimestamp = systemTimestamp.time

			val focusedDuration = if (hasFocus) 0 else currentTimestamp - _lastTimestamp

			if (!isDisposed) {
				_observer.onNext(Pair(hasFocus, focusedDuration))
			}

			_lastTimestamp = currentTimestamp
		}

		override fun onDispose() {
			_view.onFocusChangeListener = null
		}

	}

}