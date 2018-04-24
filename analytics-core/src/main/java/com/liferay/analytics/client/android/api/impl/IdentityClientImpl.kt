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

import com.liferay.analytics.client.IdentityClient
import com.liferay.analytics.client.android.util.HTTPClient
import com.liferay.analytics.client.android.util.JSONParser
import com.liferay.analytics.model.IdentityContextMessage
import java.io.IOException

/**
 * @author Igor Matos
 * @author Allan Melo
 */
class IdentityClientImpl : IdentityClient {

    private val identityGatewayHost: String
        get() = IDENTITY_GATEWAY_HOST

    private val identityGatewayPath: String
        get() = IDENTITY_GATEWAY_PATH

    private val identityGatewayPort: String
        get() = IDENTITY_GATEWAY_PORT

    private val identityGatewayProtocol: String
        get() = IDENTITY_GATEWAY_PROTOCOL

    @Throws(IOException::class)
    override fun getUserId(identityContextMessage: IdentityContextMessage): String {

        val json = JSONParser.toJSON(identityContextMessage)

        val identityPath = String.format(
                "%s://%s:%s/%s%s", identityGatewayProtocol,
                identityGatewayHost, identityGatewayPort,
                identityContextMessage.analyticsKey, identityGatewayPath)

        return HTTPClient.post(identityPath, json)
    }

    companion object {
        private const val IDENTITY_GATEWAY_HOST = "contacts-prod.liferay.com"
        private const val IDENTITY_GATEWAY_PATH = "/identity"
        private const val IDENTITY_GATEWAY_PORT = "443"
        private const val IDENTITY_GATEWAY_PROTOCOL = "https"
    }

}