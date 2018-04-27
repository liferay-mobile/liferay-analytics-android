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

package com.liferay.analytics.client.android.util

import com.liferay.analytics.client.android.BuildConfig

import java.io.IOException

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * @author Igor Matos
 */
@Config(constants = BuildConfig::class, sdk = [26])
@RunWith(RobolectricTestRunner::class)
class FileStorageTest {

	private lateinit var fileStorage: FileStorage
	private val notSavedKey = "notSavedValue"
	private val savedKey = "savedKey"
	private val savedValue = "savedValue"

	@Before
	@Throws(IOException::class)
	fun setUp() {
		fileStorage = FileStorage(RuntimeEnvironment.application)

		fileStorage.saveStringToKey(savedKey, savedValue)
	}

	@Test
	@Throws(IOException::class)
	fun testRetrieveSavedValue() {
		val result = fileStorage.getStringByKey(savedKey)

		Assert.assertNotNull(result)
		Assert.assertEquals(savedValue, result)
	}

	@Test
	fun testRetrieveUnsavedValue() {
		val result = fileStorage.getStringByKey(notSavedKey)

		result?.let {
			Assert.fail()
		}
	}

	@Test
	@Throws(IOException::class)
	fun testSaveStringToKey() {
		fileStorage.saveStringToKey("key", "value")
	}
}