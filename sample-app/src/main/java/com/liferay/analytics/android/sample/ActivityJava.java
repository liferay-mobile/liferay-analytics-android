/*
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

package com.liferay.analytics.android.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import com.liferay.analytics.android.forms.FieldAttributes;
import com.squareup.leakcanary.RefWatcher;
import com.liferay.analytics.android.forms.FormAttributes;
import com.liferay.analytics.android.forms.Forms;

public class ActivityJava extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        RefWatcher refWatcher = MainApplication.Companion.getRefWatcher(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FormAttributes formAttributes = new FormAttributes("FormID", "Form Title");

        FieldAttributes nameFieldAttributes = new FieldAttributes("Name", "nameTitle", formAttributes);
        EditText nameField = findViewById(R.id.nameField);
        refWatcher.watch(nameField);

        Forms.trackField(nameField, nameFieldAttributes);

        FieldAttributes emailFieldAttributes = new FieldAttributes("Email", "emailTitle", formAttributes);
        EditText emailField = findViewById(R.id.emailField);
        Forms.trackField(emailField, emailFieldAttributes);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Forms.formSubmitted(formAttributes);

                Snackbar.make(view, "Email sent!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            }
        });
    }
}
