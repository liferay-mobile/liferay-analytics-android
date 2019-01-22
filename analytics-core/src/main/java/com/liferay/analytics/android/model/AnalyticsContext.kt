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

package com.liferay.analytics.android.model

import android.content.Context
import android.content.pm.PackageManager
import org.koin.standalone.KoinComponent
import java.util.*
import android.view.WindowManager
import android.util.DisplayMetrics

/**
 * @author Victor Oliveira
 */
internal class AnalyticsContext(context: Context) : KoinComponent {
    val contentLanguageId = Locale.getDefault().toString()
    val languageId = Locale.getDefault().toString()
    val screenHeight: Int
    val screenWidth: Int
    val userAgent: String

    init {
        val metrics = DisplayMetrics()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        windowManager.defaultDisplay.getMetrics(metrics)

        screenHeight = metrics.heightPixels
        screenWidth = metrics.widthPixels

        val labelRes = context.applicationInfo.labelRes
        val applicationName = if (labelRes != 0) context.getString(labelRes) else ""
        val applicationVersion = packageInfo.versionName

        userAgent = "${System.getProperty("http.agent")} $applicationName/$applicationVersion"
    }
}