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
import java.util.*

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
    fun formSubmitted(formContext: FormContext) {
        val eventId = "formSubmitted"

        val properties = HashMap<String, String>()

        formContext.formTitle?.let {
            properties[FORM_TITLE_KEY] = it
        }

        properties[FORM_ID_KEY] = formContext.formId

        val analyticsInstance = Analytics.instance

        analyticsInstance.send(eventId, properties, APPLICATION_ID)
    }

    @JvmStatic
    fun formViewed(formContext: FormContext) {
        val eventId = "formViewed"

        val properties = HashMap<String, String>()

        formContext.formTitle?.let {
            properties[FORM_TITLE_KEY] = it
        }

        properties[FORM_ID_KEY] = formContext.formId

        val analyticsInstance = Analytics.instance

        analyticsInstance.send(eventId, properties, APPLICATION_ID)
    }

    @JvmStatic
    fun trackField(editText: EditText, fieldContext: FieldContext) {

        RxViewUtil.onFocus(
                editText).subscribe { focusEvent ->
            val hasFocus = focusEvent.first

            if (hasFocus!!) {
                _fieldFocused(fieldContext)
            } else {
                val focusDuration = focusEvent.second

                _fieldBlurred(focusDuration!!, fieldContext)
            }
        }
    }

    private fun _fieldBlurred(focusDuration: Long, fieldContext: FieldContext) {
        val eventId = "fieldBlurred"

        val properties = HashMap<String, String>()

        fieldContext.title?.let {
            properties[FIELD_TITLE_KEY] = it
        }

        properties[FORM_ID_KEY] = fieldContext.formContext.formId
        properties[FIELD_NAME] = fieldContext.name
        properties[FOCUS_DURATION_KEY] = focusDuration.toString()

        val analyticsInstance = Analytics.instance

        analyticsInstance.send(eventId, properties, APPLICATION_ID)
    }

    private fun _fieldFocused(fieldContext: FieldContext) {
        val eventId = "fieldFocused"

        val properties = HashMap<String, String>()

        fieldContext.title?.let {
            properties[FIELD_TITLE_KEY] = it
        }

        properties[FORM_ID_KEY] = fieldContext.formContext.formId
        properties[FIELD_NAME] = fieldContext.name

        val analyticsInstance = Analytics.instance

        analyticsInstance.send(eventId, properties, APPLICATION_ID)
    }

}