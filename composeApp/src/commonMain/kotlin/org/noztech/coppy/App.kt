package org.noztech.coppy

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import org.noztech.coppy.core.AppSettings
import org.noztech.coppy.core.ui.theme.AppTheme
import org.noztech.coppy.navigation.AppNavHost

@Composable
@Preview
fun App() {
    val appSettings: AppSettings = getKoin().get()
    AppTheme {
        AppNavHost(appSettings = appSettings)
    }
}