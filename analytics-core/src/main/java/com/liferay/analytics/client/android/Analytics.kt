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

import android.app.Application
import android.content.Context
import com.liferay.analytics.client.android.dao.UserDAO
import com.liferay.analytics.client.android.model.Event
import com.liferay.analytics.client.android.model.Identity
import com.liferay.analytics.client.android.model.IdentityContext
import com.liferay.analytics.client.android.util.FileStorage
import com.liferay.analytics.client.android.util.FlushProcess
import io.reactivex.annotations.NonNull

/**
 * @author Igor Matos
 */
class Analytics private constructor(fileStorage: FileStorage, internal val analyticsKey: String,
	flushIntervalInMilliseconds: Long) {

	private val userDAO: UserDAO

	init {
		analyticsInstance?.let {
			throw IllegalStateException("Your library was already initialized.")
		}

		analyticsInstance = this

		userDAO = UserDAO(fileStorage)
		flushProcess = FlushProcess(fileStorage, flushIntervalInMilliseconds)
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
			val fileStorage = FileStorage(application)

			Analytics(fileStorage, analyticsKey, flushIntervalInMilliseconds)
		}

		@JvmOverloads
		@JvmStatic
		fun send(@NonNull eventId: String, @NonNull applicationId: String,
			@NonNull properties: Map<String, String> = hashMapOf()) {

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

			flushProcess.addEvent(event)
		}

		@JvmStatic
		var instance: Analytics?
			@Synchronized get() {
				analyticsInstance?.let {
					return it
				}

				throw IllegalStateException("You must initialize your library")
			}
			set(value) {
				analyticsInstance = value
			}

		@JvmStatic
		fun setIdentity(email: String, name: String = "") {
			val identityContext = instance!!.getDefaultIdentityContext()

			val identity = Identity(name, email)
			identityContext.identity = identity

			instance!!.userDAO.addIdentityContext(identityContext)
			instance!!.userDAO.setUserId(identityContext.userId)
		}

		private var analyticsInstance: Analytics? = null

		internal lateinit var flushProcess: FlushProcess
		private const val FLUSH_INTERVAL_DEFAULT: Long = 60000
	}

	fun getDefaultIdentityContext(): IdentityContext {
		return IdentityContext(analyticsKey)
	}

}