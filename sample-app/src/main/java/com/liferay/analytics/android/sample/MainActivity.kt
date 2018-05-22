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

package com.liferay.analytics.android.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.liferay.analytics.android.forms.FormAttributes
import com.liferay.analytics.android.forms.Forms

/**
 * @author Igor Matos
 */
class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val formAttributes = FormAttributes("FormID", "Form Title")

		Forms.formViewed(formAttributes)

		val button = findViewById<Button>(R.id.button)

		button.setOnClickListener {
			var intent = Intent(it.context, ActivityJava::class.java)
			startActivity(intent)
		}
	}

}