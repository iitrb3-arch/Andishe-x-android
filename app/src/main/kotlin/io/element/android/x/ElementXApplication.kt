/*
 * Copyright 2022-2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.x

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.startup.AppInitializer
import androidx.core.os.LocaleListCompat
import dev.zacsweers.metro.createGraphFactory
import io.element.android.features.cachecleaner.api.CacheCleanerInitializer
import io.element.android.libraries.di.DependencyInjectionGraphOwner
import io.element.android.x.di.AppGraph
import io.element.android.x.info.logApplicationInfo
import io.element.android.x.initializer.CrashInitializer
import io.element.android.x.initializer.PlatformInitializer
import java.util.Locale

class ElementXApplication : Application(), DependencyInjectionGraphOwner {
    override val graph: AppGraph = createGraphFactory<AppGraph.Factory>().create(this)

    override fun onCreate() {
        super.onCreate()
        val defaultLocale = Locale("fa")
        Locale.setDefault(defaultLocale)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(defaultLocale))
        AppInitializer.getInstance(this).apply {
            initializeComponent(CrashInitializer::class.java)
            initializeComponent(PlatformInitializer::class.java)
            initializeComponent(CacheCleanerInitializer::class.java)
        }
        logApplicationInfo(this)
    }
}
