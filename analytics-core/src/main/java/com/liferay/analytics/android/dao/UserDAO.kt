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

package com.liferay.analytics.android.dao

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liferay.analytics.android.model.IdentityContext
import com.liferay.analytics.android.util.FileStorage
import java.io.IOException
import java.lang.reflect.Type

/**
 * @author Igor Matos
 */
internal class UserDAO(private var fileStorage: FileStorage) {

	fun addIdentityContext(identityContext: IdentityContext) {
		replace(getUserContexts().plus(identityContext))
	}

	fun clearIdentities() {
		replace(listOf())
	}

	fun clearUserId() {
		replace(STORAGE_KEY_USER_ID, "")
	}

	fun getUserContexts(): List<IdentityContext> {
		val userContextsJsonString = fileStorage.getStringByKey(USER_CONTEXTS)

		userContextsJsonString?.let {
			try {
				return Gson().fromJson<List<IdentityContext>>(it, listType())
			}
			catch (e: JsonSyntaxException) {
				clearIdentities()
			}
		}

		return listOf()
	}

	fun getUserId(): String? {
		return fileStorage.getStringByKey(STORAGE_KEY_USER_ID)
	}

	fun replace(identityContexts: List<IdentityContext>) {
		val eventsJson = Gson().toJson(identityContexts)
		replace(USER_CONTEXTS, eventsJson)
	}

	fun replace(key: String, value: String) {
		try {
			fileStorage.saveStringToKey(key, value)
		}
		catch (e: IOException) {
			Log.d("LIFERAY-ANALYTICS",
				"Could not replace the value for key ${e.localizedMessage}")
		}
	}

	fun setUserId(userId: String) {
		try {
			fileStorage.saveStringToKey(STORAGE_KEY_USER_ID, userId)
		}
		catch (e: IOException) {
			Log.d("LIFERAY-ANALYTICS", "Could not save UserId ${e.localizedMessage}")
		}
	}

	private fun listType(): Type? {
		return object : TypeToken<ArrayList<IdentityContext>>() {}.type
	}

	companion object {
		private const val STORAGE_KEY_USER_ID = "lcs_client_user_id"
		private const val USER_CONTEXTS = "user_contexts"
	}
}