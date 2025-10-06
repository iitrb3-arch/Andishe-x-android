/*
 * Copyright 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.enterprise.impl

import com.google.common.truth.Truth.assertThat
import io.element.android.appconfig.AuthenticationConfig
import io.element.android.libraries.matrix.test.A_SESSION_ID
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DefaultEnterpriseServiceTest {
    @Test
    fun `isEnterpriseBuild is false`() {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isEnterpriseBuild).isFalse()
    }

    @Test
    fun `defaultHomeserverList should return only the configured homeserver`() {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.defaultHomeserverList())
            .containsExactly(AuthenticationConfig.MATRIX_ORG_URL.removeSuffix("/"))
    }

    @Test
    fun `isAllowedToConnectToHomeserver is true only for the configured homeserver`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isAllowedToConnectToHomeserver(AuthenticationConfig.MATRIX_ORG_URL)).isTrue()
        assertThat(defaultEnterpriseService.isAllowedToConnectToHomeserver("edu97.ir")).isTrue()
        assertThat(defaultEnterpriseService.isAllowedToConnectToHomeserver("https://matrix.org")).isFalse()
    }

    @Test
    fun `isEnterpriseUser always return false`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isEnterpriseUser(A_SESSION_ID)).isFalse()
    }
}
