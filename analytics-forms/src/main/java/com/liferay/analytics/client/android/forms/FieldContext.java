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

package com.liferay.analytics.client.android.forms;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Igor Matos
 */
public class FieldContext {

	public FieldContext(
		@NonNull String name, @Nullable String title,
		@NonNull FormContext formContext) {

		_name = name;
		_title = title;
		_formContext = formContext;
	}

	@NonNull
	public FormContext getFormContext() {
		return _formContext;
	}

	@NonNull
	public String getName() {
		return _name;
	}

	@Nullable
	public String getTitle() {
		return _title;
	}

	@NonNull
	private final FormContext _formContext;

	@NonNull
	private final String _name;

	@Nullable
	private final String _title;

}