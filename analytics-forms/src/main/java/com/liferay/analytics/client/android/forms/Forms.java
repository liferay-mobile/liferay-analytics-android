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

import android.util.Pair;

import android.widget.EditText;

import com.liferay.analytics.client.android.Analytics;

import io.reactivex.functions.Consumer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Matos
 */
public class Forms {

	public static void formSubmitted(@NonNull FormContext formContext) {
		String eventId = "formSubmitted";

		Map<String, String> properties = new HashMap<>();

		properties.put(_FORM_ID_KEY, formContext.getFormId());

		Analytics analyticsInstance = Analytics.getInstance();

		analyticsInstance.send(eventId, properties, _APPLICATION_ID);
	}

	public static void formViewed(@NonNull FormContext formContext) {
		String eventId = "formViewed";

		Map<String, String> properties = new HashMap<>();

		properties.put(_FORM_ID_KEY, formContext.getFormId());

		Analytics analyticsInstance = Analytics.getInstance();

		analyticsInstance.send(eventId, properties, _APPLICATION_ID);
	}

	public static void trackField(
		@NonNull final EditText editText,
		@NonNull final FieldContext fieldContext) {

		RxViewUtil.onFocus(
			editText).subscribe(new Consumer<Pair<Boolean, Long>>() {

			@Override
			public void accept(Pair<Boolean, Long> focusEvent)
				throws Exception {
				Boolean hasFocus = focusEvent.first;

				if (hasFocus) {
					_fieldFocused(fieldContext);
				}
				else {
					Long focusDuration = focusEvent.second;

					_fieldBlurred(focusDuration, fieldContext);
				}
			}

		});
	}

	private static void _fieldBlurred(
		@NonNull long focusDuration, @NonNull FieldContext fieldContext) {

		String eventId = "fieldBlurred";

		Map<String, String> properties = new HashMap<>();

		properties.put(_FORM_ID_KEY, fieldContext.getFormContext().getFormId());
		properties.put(_FIELD_NAME, fieldContext.getName());
		properties.put(_FOCUS_DURATION_KEY, String.valueOf(focusDuration));

		Analytics analyticsInstance = Analytics.getInstance();

		analyticsInstance.send(eventId, properties, _APPLICATION_ID);
	}

	private static void _fieldFocused(@NonNull FieldContext fieldContext) {
		String eventId = "fieldFocused";

		Map<String, String> properties = new HashMap<>();

		properties.put(_FORM_ID_KEY, fieldContext.getFormContext().getFormId());
		properties.put(_FIELD_NAME, fieldContext.getName());

		Analytics analyticsInstance = Analytics.getInstance();

		analyticsInstance.send(eventId, properties, _APPLICATION_ID);
	}

	private static final String _APPLICATION_ID = "forms";

	private static final String _FIELD_NAME = "fieldName";

	private static final String _FOCUS_DURATION_KEY = "focusDuration";

	private static final String _FORM_ID_KEY = "formId";

}