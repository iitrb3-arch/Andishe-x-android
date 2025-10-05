/*
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.login.impl.accountprovider

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.element.android.appconfig.AuthenticationConfig
import io.element.android.features.enterprise.api.EnterpriseService
import io.element.android.features.enterprise.test.FakeEnterpriseService
import io.element.android.tests.testutils.WarmUpRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class AccountProviderDataSourceTest {
    @get:Rule
    val warmUpRule = WarmUpRule()

    private val defaultHomeserverDomain = AuthenticationConfig.DEFAULT_HOMESERVER_URL.removePrefix("https://")

    @Test
    fun `present - initial state`() = runTest {
        val sut = AccountProviderDataSource(FakeEnterpriseService())
        sut.flow.test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(
                AccountProvider(
                    url = AuthenticationConfig.DEFAULT_HOMESERVER_URL,
                    title = defaultHomeserverDomain,
                    subtitle = null,
                    isPublic = false,
                    isMatrixOrg = false,
                    isValid = false,
                )
            )
        }
    }

    @Test
    fun `present - initial state - matrix org`() = runTest {
        val sut = AccountProviderDataSource(
            FakeEnterpriseService(
                defaultHomeserverListResult = { listOf(AuthenticationConfig.DEFAULT_HOMESERVER_URL) }
            )
        )
        sut.flow.test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(
                AccountProvider(
                    url = AuthenticationConfig.DEFAULT_HOMESERVER_URL,
                    title = defaultHomeserverDomain,
                    subtitle = null,
                    isPublic = false,
                    isMatrixOrg = false,
                    isValid = false,
                )
            )
        }
    }

    @Test
    fun `present - ensure that default homeserver is not star char`() = runTest {
        val sut = AccountProviderDataSource(
            FakeEnterpriseService(
                defaultHomeserverListResult = { listOf(EnterpriseService.ANY_ACCOUNT_PROVIDER, AuthenticationConfig.DEFAULT_HOMESERVER_URL) }
            )
        )
        sut.flow.test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(
                AccountProvider(
                    url = AuthenticationConfig.DEFAULT_HOMESERVER_URL,
                    title = defaultHomeserverDomain,
                    subtitle = null,
                    isPublic = false,
                    isMatrixOrg = false,
                    isValid = false,
                )
            )
        }
    }

    @Test
    fun `present - user change and reset`() = runTest {
        val sut = AccountProviderDataSource(FakeEnterpriseService())
        sut.flow.test {
            val initialState = awaitItem()
            assertThat(initialState.url).isEqualTo(AuthenticationConfig.DEFAULT_HOMESERVER_URL)
            sut.userSelection(AccountProvider(url = "https://example.com"))
            val changedState = awaitItem()
            assertThat(changedState).isEqualTo(
                AccountProvider(
                    url = "https://example.com",
                    title = "example.com",
                    subtitle = null,
                    isPublic = false,
                    isMatrixOrg = false,
                    isValid = false,
                )
            )
            sut.reset()
            val resetState = awaitItem()
            assertThat(resetState.url).isEqualTo(AuthenticationConfig.DEFAULT_HOMESERVER_URL)
        }
    }
}
