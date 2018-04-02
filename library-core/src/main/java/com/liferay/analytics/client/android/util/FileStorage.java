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

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Igor Matos
 */
public class FileStorage {

	public FileStorage(Context context) {
		_context = context;
	}

	public String getStringByKey(String key) throws IOException {
		FileInputStream inputStream = _context.openFileInput(key);

		InputStreamReader inputStreamReader = new InputStreamReader(
			inputStream);

		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		StringBuilder stringBuilder = new StringBuilder();
		String line;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}

		return String.valueOf(stringBuilder);
	}

	public void saveStringToKey(String key, String value) throws IOException {
		FileOutputStream outputStream = _context.openFileOutput(
			key, Context.MODE_PRIVATE);

		outputStream.write(value.getBytes());
		outputStream.close();
	}

	private final Context _context;

}