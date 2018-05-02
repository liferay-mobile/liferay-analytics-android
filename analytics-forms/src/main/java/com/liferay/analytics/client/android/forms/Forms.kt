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

package com.liferay.analytics.client.android.forms

import android.widget.EditText
import com.liferay.analytics.client.android.Analytics
import com.liferay.analytics.client.android.forms.FormEvent.*

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
	fun trackField(editText: EditText, fieldContext: FieldContext) {

		RxViewUtil.onFocus(editText).subscribe { focusEvent ->
			val hasFocus = focusEvent.first

			if (hasFocus) {
				fieldFocused(fieldContext)
			} else {
				val focusDuration = focusEvent.second

				fieldBlurred(focusDuration, fieldContext)
			}
		}
	}

	private fun fieldBlurred(focusDuration: Long, fieldContext: FieldContext) {
		val properties = HashMap<String, String>()

		fieldContext.title?.let {
			properties[FIELD_TITLE_KEY] = it
		}

		properties[FORM_ID_KEY] = fieldContext.formAttributes.formId
		properties[FIELD_NAME] = fieldContext.name
		properties[FOCUS_DURATION_KEY] = focusDuration.toString()

		send(FIELD_BLURRED, properties)
	}

	private fun fieldFocused(fieldContext: FieldContext) {
		val properties = HashMap<String, String>()

		fieldContext.title?.let {
			properties[FIELD_TITLE_KEY] = it
		}

		properties[FORM_ID_KEY] = fieldContext.formAttributes.formId
		properties[FIELD_NAME] = fieldContext.name

		send(FIELD_FOCUSED, properties)
	}

	private fun send(formEvent: FormEvent, properties: HashMap<String, String>) {
		Analytics.send(formEvent.value, APPLICATION_ID, properties)
	}

}