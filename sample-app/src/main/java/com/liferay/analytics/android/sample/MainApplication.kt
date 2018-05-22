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

package com.liferay.analytics.android.sample

import android.app.Application
import android.content.Context
import com.liferay.analytics.android.Analytics
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

/**
 * @author Igor Matos
 */
class MainApplication : Application() {

	companion object {
		fun getRefWatcher(context: Context): RefWatcher {
			val application = context.applicationContext as MainApplication
			return application.refWatcher
		}
	}

	private lateinit var refWatcher: RefWatcher

	override fun onCreate() {
		super.onCreate()

		if (LeakCanary.isInAnalyzerProcess(this)) {
			return
		}

		if (BuildConfig.DEBUG) {
			refWatcher = LeakCanary.install(this)
		}

		Analytics.configure(this, "key")

	}

}