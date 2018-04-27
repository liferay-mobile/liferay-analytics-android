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

package com.liferay.analytics.client.android.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.liferay.analytics.client.android.api.impl.IdentityClientImpl
import com.liferay.analytics.client.android.forms.FormContext
import com.liferay.analytics.client.android.forms.Forms

/**
 * @author Igor Matos
 */
class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val formContext = FormContext("FormID", "Form Title")

		Forms.formViewed(formContext)

		val button = findViewById<Button>(R.id.button)

		button.setOnClickListener {
			var intent = Intent(it.context, ActivityJava::class.java)
			startActivity(intent)
		}
	}

}