/*
 * Copyright 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.enterprise.impl

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.element.android.appconfig.AuthenticationConfig
import io.element.android.compound.tokens.generated.SemanticColors
import io.element.android.compound.tokens.generated.compoundColorsDark
import io.element.android.compound.tokens.generated.compoundColorsLight
import io.element.android.features.enterprise.api.BugReportUrl
import io.element.android.features.enterprise.api.EnterpriseService
import io.element.android.libraries.matrix.api.core.SessionId
import kotlinx.coroutines.flow.flowOf

@ContributesBinding(AppScope::class)
@Inject
class DefaultEnterpriseService : EnterpriseService {
    private val allowedHomeserver = normalize(AuthenticationConfig.MATRIX_ORG_URL)

    override val isEnterpriseBuild = false

    override suspend fun isEnterpriseUser(sessionId: SessionId) = false

    override fun defaultHomeserverList(): List<String> = listOf(allowedHomeserver)
    override suspend fun isAllowedToConnectToHomeserver(homeserverUrl: String): Boolean {
        return normalize(homeserverUrl).equals(allowedHomeserver, ignoreCase = true)
    }

    override fun semanticColorsLight(): SemanticColors = compoundColorsLight

    override fun semanticColorsDark(): SemanticColors = compoundColorsDark

    override fun firebasePushGateway(): String? = null
    override fun unifiedPushDefaultPushGateway(): String? = null

    override val bugReportUrlFlow = flowOf(BugReportUrl.UseDefault)

    private fun normalize(url: String): String {
        val trimmed = url.trim()
        val withScheme = when {
            trimmed.isEmpty() -> trimmed
            trimmed.startsWith("http", ignoreCase = true) -> trimmed
            else -> "https://$trimmed"
        }
        return withScheme.removeSuffix("/")
    }
}
