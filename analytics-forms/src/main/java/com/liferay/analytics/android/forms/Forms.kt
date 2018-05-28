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

package com.liferay.analytics.android.forms

import android.arch.lifecycle.LifecycleOwner
import android.widget.EditText
import com.jakewharton.rxbinding2.view.focusChanges
import com.liferay.analytics.android.Analytics
import com.liferay.analytics.android.forms.FormEvent.FIELD_BLURRED
import com.liferay.analytics.android.forms.FormEvent.FIELD_FOCUSED
import com.liferay.analytics.android.forms.FormEvent.FORM_SUBMITTED
import com.liferay.analytics.android.forms.FormEvent.FORM_VIEWED
import java.sql.Timestamp

/**
 * @author Igor Matos
 */
object Forms {
	private const val APPLICATION_ID = "forms"
	private const val FIELD_NAME = "fieldName"
	private const val FOCUS_DURATION_KEY = "focusDuration"
	private const val FORM_ID_KEY = "formId"
	private const val FORM_TITLE_KEY = "formTitle"
	private const val FIELD_TITLE_KEY = "fieldTitle"

	@JvmStatic
	fun formSubmitted(formAttributes: FormAttributes) {
		val properties = HashMap<String, String>()

		formAttributes.formTitle?.let {
			properties[FORM_TITLE_KEY] = it
		}

		properties[FORM_ID_KEY] = formAttributes.formId

		send(FORM_SUBMITTED, properties)
	}

	@JvmStatic
	fun formViewed(formAttributes: FormAttributes) {
		val properties = HashMap<String, String>()

		formAttributes.formTitle?.let {
			properties[FORM_TITLE_KEY] = it
		}

		properties[FORM_ID_KEY] = formAttributes.formId

		send(FORM_VIEWED, properties)
	}

	@JvmStatic
	fun trackField(editText: EditText, fieldAttributes: FieldAttributes) {
		val lifecycleOwner = editText.context as? LifecycleOwner ?: return

		editText.focusChanges()
			.skipInitialValue()
			.doOnNext { focus ->
				if (focus) {
					fieldFocused(fieldAttributes)
				}
			}
			.map { Timestamp(System.currentTimeMillis()).time }
			.buffer(2)
			.map { pair ->
				val focusTimestamp = pair.first()
				val blurredTimestamp = pair.last()
				blurredTimestamp - focusTimestamp
			}
			.subscribe { duration ->
				fieldBlurred(duration, fieldAttributes)
			}
			.disposedWith(lifecycleOwner)
	}

	private fun fieldBlurred(focusDuration: Long, fieldAttributes: FieldAttributes) {
		val properties = HashMap<String, String>()

		fieldAttributes.title?.let {
			properties[FIELD_TITLE_KEY] = it
		}

		properties[FORM_ID_KEY] = fieldAttributes.formAttributes.formId
		properties[FIELD_NAME] = fieldAttributes.name
		properties[FOCUS_DURATION_KEY] = focusDuration.toString()

		send(FIELD_BLURRED, properties)
	}

	private fun fieldFocused(fieldAttributes: FieldAttributes) {
		val properties = HashMap<String, String>()

		fieldAttributes.title?.let {
			properties[FIELD_TITLE_KEY] = it
		}

		properties[FORM_ID_KEY] = fieldAttributes.formAttributes.formId
		properties[FIELD_NAME] = fieldAttributes.name

		send(FIELD_FOCUSED, properties)
	}

	private fun send(formEvent: FormEvent, properties: HashMap<String, String>) {
		Analytics.send(formEvent.value, APPLICATION_ID, properties)
	}
}