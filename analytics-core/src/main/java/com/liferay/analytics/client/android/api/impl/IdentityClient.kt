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

package com.liferay.analytics.client.android.api.impl

import com.liferay.analytics.client.android.model.IdentityContext
import com.liferay.analytics.client.android.util.HTTPClient
import com.liferay.analytics.client.android.util.JSONParser
import java.io.IOException

/**
 * @author Igor Matos
 * @author Allan Melo
 */
internal class IdentityClient {

	private val identityGatewayHost: String
		get() = IDENTITY_GATEWAY_HOST

	private val identityGatewayPath: String
		get() = IDENTITY_GATEWAY_PATH

	private val identityGatewayPort: String
		get() = IDENTITY_GATEWAY_PORT

	private val identityGatewayProtocol: String
		get() = IDENTITY_GATEWAY_PROTOCOL

	@Throws(IOException::class)
	fun send(identityContext: IdentityContext): String {

		val json = JSONParser.toJSON(identityContext)

		val identityPath = "$identityGatewayProtocol://$identityGatewayHost:" +
			"$identityGatewayPort/$identityGatewayPath"

		return HTTPClient.post(identityPath, json)
	}

	companion object {
		private const val IDENTITY_GATEWAY_HOST = "ec-dev.liferay.com"
		private const val IDENTITY_GATEWAY_PATH = "api/identitycontextgateway/send-identity-context"
		private const val IDENTITY_GATEWAY_PORT = "8095"
		private const val IDENTITY_GATEWAY_PROTOCOL = "https"
	}

}