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
public class FormContext {

	public FormContext(@NonNull String formId, @Nullable String formTitle) {
		_formId = formId;
		_formTitle = formTitle;
	}

	@NonNull
	public String getFormId() {
		return _formId;
	}

	@Nullable
	public String getFormTitle() {
		return _formTitle;
	}

	@NonNull
	private final String _formId;

	@Nullable
	private final String _formTitle;

}