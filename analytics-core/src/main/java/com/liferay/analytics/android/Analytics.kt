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

package com.liferay.analytics.android

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.liferay.analytics.android.dao.UserDAO
import com.liferay.analytics.android.model.AnalyticsContext
import com.liferay.analytics.android.model.Event
import com.liferay.analytics.android.model.Identity
import com.liferay.analytics.android.model.IdentityContext
import com.liferay.analytics.android.util.FileStorage
import com.liferay.analytics.android.util.FlushProcess
import okhttp3.HttpUrl
import org.jetbrains.annotations.NotNull
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext.startKoin

/**
 * @author Igor Matos
 */
class Analytics private constructor(
		endpointURL: String, internal val dataSourceId: String,
		fileStorage: FileStorage, flushInterval: Long) {

	companion object {

		/**
		 * Need to call to initialize the library
		 *
		 * @throws IllegalStateException if the library was already initialized
		 * @since 1.0.0
		 */
		@JvmOverloads
		@JvmStatic
		fun init(
			@NotNull context: Context, flushInterval: Int = FLUSH_INTERVAL_DEFAULT) {

			analyticsInstance?.let {
				throw IllegalStateException("Your library was already initialized.")
			}

			if (context == null) {
				throw IllegalArgumentException("Context can't be null.")
			}

			if (context.applicationContext == null) {
				throw IllegalArgumentException("AppContext can't be null.")
			}

			val applicationInfo =
				context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)

			val dataSourceId = applicationInfo.metaData.getString("com.liferay.analytics.DataSourceId")

			if (dataSourceId.isNullOrEmpty() || dataSourceId.isBlank()) {
				throw IllegalArgumentException("Analytics Data source id can't be null or empty.")
			}

			val endpointURL = applicationInfo.metaData.getString("com.liferay.analytics.EndpointUrl")

			if (endpointURL.isNullOrEmpty() || endpointURL.isBlank() || HttpUrl.parse(endpointURL) == null) {
				throw IllegalArgumentException("You must provide a valid endpoint URL")
			}

			if (flushInterval <= 0) {
				throw IllegalArgumentException("flushInterval can't be less than or equals zero.")
			}

			val application = context.applicationContext as Application
			val fileStorage = FileStorage(application)

			startKoin(getModules(context))

			analyticsInstance = Analytics(endpointURL, dataSourceId, fileStorage, flushInterval.toLong())
		}

		/**
		 * Send custom events to Liferay Analytics
		 *
		 * @throws IllegalStateException if the library was not initialized
		 * @since 1.0.0
		 */
		@JvmOverloads
		@JvmStatic
		fun send(@NotNull eventId: String, @NotNull applicationId: String,
			@NotNull properties: Map<String, String> = hashMapOf()) {

			if (eventId.isNullOrEmpty() || eventId.isBlank()) {
				throw IllegalArgumentException("EventId can't be null or empty.")
			}

			if (applicationId.isNullOrEmpty() || applicationId.isBlank()) {
				throw IllegalArgumentException("ApplicationId can't be null or empty.")
			}

			if (properties == null) {
				throw IllegalArgumentException("Properties can't be null.")
			}

			val event = Event(applicationId, eventId)
			event.properties = properties

			instance.flushProcess.addEvent(event)
		}

		/**
		 * Need to call to send events with user informations.
		 * Recommended after the user login in application.
		 *
		 * @throws IllegalStateException if the library was not initialized
		 * @since 1.0.0
		 */
		@JvmStatic
		fun setIdentity(email: String, name: String = "") {
			val identityContext = IdentityContext(instance.dataSourceId)

			val identity = Identity(name, email)
			identityContext.identity = identity

			instance.userDAO.addIdentityContext(identityContext)
			instance.userDAO.setUserId(identityContext.userId)
		}

		/**
		 * Need to call to clear the identity, to send events with anonymous session again.
		 * Recommended after the user logout of application.
		 *
		 * @throws IllegalStateException if the library was not initialized
		 * @since 1.0.0
		 */
		@JvmStatic
		fun clearSession() {
			instance.userDAO.clearUserId()
		}

		@JvmStatic
		internal fun close() {
			analyticsInstance = null
		}

		/**
		 * Returns the Liferay Analytics instance
		 *
		 * @throws IllegalStateException if the library was not initialized
		 * @since 1.0.0
		 */
		@JvmStatic
		internal var instance: Analytics
			@Synchronized get() {
				analyticsInstance?.let {
					return it
				}

				throw IllegalStateException("You must initialize your library")
			}
			set(value) {
				analyticsInstance = value
			}

		internal fun getModules(context: Context): List<Module> {
			val contextModule: Module = applicationContext {
				bean { context.applicationContext as Context }
			}

			val analyticsContextModule: Module = applicationContext {
				bean { AnalyticsContext(context) }
			}

			return listOf(contextModule, analyticsContextModule)
		}

		private var analyticsInstance: Analytics? = null
		private const val FLUSH_INTERVAL_DEFAULT: Int = 60
	}

	internal val userDAO = UserDAO(fileStorage)
	internal var flushProcess = FlushProcess(endpointURL, fileStorage, flushInterval)
}