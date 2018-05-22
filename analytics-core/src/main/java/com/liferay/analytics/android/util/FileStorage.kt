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

package com.liferay.analytics.android.util

import android.content.Context
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

/**
 * @author Igor Matos
 */
internal class FileStorage(private val context: Context) {

	@Throws(IOException::class)
	fun getStringByKey(key: String): String? {
		try {
			val inputStream = context.openFileInput(key)

			val inputStreamReader = InputStreamReader(inputStream)

			val bufferedReader = BufferedReader(inputStreamReader)

			val stringBuilder = StringBuilder()

			bufferedReader.lineSequence().forEach {
				stringBuilder.append(it)
			}

			return stringBuilder.toString()
		} catch (e: FileNotFoundException) {
			return null
		}
	}

	@Throws(IOException::class)
	fun saveStringToKey(key: String, value: String) {
		val outputStream = context.openFileOutput(key, Context.MODE_PRIVATE)

		outputStream.write(value.toByteArray())
		outputStream.close()
	}

}