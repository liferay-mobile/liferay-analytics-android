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

package com.liferay.analytics.client.android.util;

import com.liferay.analytics.client.android.BuildConfig;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * @author Igor Matos
 */
@Config(constants = BuildConfig.class, sdk = 26)
@RunWith(RobolectricTestRunner.class)
public class FileStorageTest {

    @Before
    public void setUp() throws IOException {
        _fileStorage = new FileStorage(RuntimeEnvironment.application);

        _fileStorage.saveStringToKey(_savedKey, _savedValue);
    }

    @Test
    public void testRetrieveSavedValue() throws IOException {
        String result = _fileStorage.getStringByKey(_savedKey);

        Assert.assertNotNull(result);
        Assert.assertEquals(_savedValue, result);
    }

    @Test
    public void testRetrieveUnsavedValue() {
        try {
            String result = _fileStorage.getStringByKey(_notSavedKey);
            Assert.fail();
        } catch (IOException ioe) {
            return;
        }
    }

    @Test
    public void testSaveStringToKey() throws IOException {
        _fileStorage.saveStringToKey("key", "value");
    }

    private FileStorage _fileStorage;
    private String _notSavedKey = "notSavedValue";
    private String _savedKey = "savedKey";
    private String _savedValue = "savedValue";
}