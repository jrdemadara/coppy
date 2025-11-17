package org.noztech.coppy.feature.welcome.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.noztech.coppy.core.AppSettings
import kotlin.time.ExperimentalTime

class WelcomeViewModel(
    private val appSettings: AppSettings
): ViewModel() {
    @OptIn(ExperimentalTime::class)
    fun firstLaunch(){
        viewModelScope.launch {
            appSettings.setFirstLaunch()
        }
    }
}