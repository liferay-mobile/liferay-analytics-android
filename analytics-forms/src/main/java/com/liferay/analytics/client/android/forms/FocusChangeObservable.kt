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
class FocusChangeObservable(private val view: View) : Observable<Pair<Boolean, Long>>() {

	override fun subscribeActual(observer: Observer<in Pair<Boolean, Long>>) {

		val listener = FocusChangeObservable.Listener(view, observer)

		observer.onSubscribe(listener)
		view.onFocusChangeListener = listener
	}

	private class Listener(
		private val view: View, private val observer: Observer<in Pair<Boolean, Long>>)
		: MainThreadDisposable(), View.OnFocusChangeListener {

		private var lastTimestamp = java.lang.Long.MIN_VALUE

		override fun onFocusChange(v: View, hasFocus: Boolean) {
			val systemTimestamp = Timestamp(System.currentTimeMillis())

			val currentTimestamp = systemTimestamp.time

			val focusedDuration = if (hasFocus) 0 else currentTimestamp - lastTimestamp

			if (!isDisposed) {
				observer.onNext(Pair(hasFocus, focusedDuration))
			}

			lastTimestamp = currentTimestamp
		}

		override fun onDispose() {
			view.onFocusChangeListener = null
		}

	}

}