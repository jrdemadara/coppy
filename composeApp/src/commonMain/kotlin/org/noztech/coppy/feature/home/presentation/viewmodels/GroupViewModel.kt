package org.noztech.coppy.feature.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.noztech.VaultGroup
import org.noztech.coppy.feature.home.domain.usecase.CreateGroupUseCase
import org.noztech.coppy.feature.home.domain.usecase.GetGroupsUseCase
import org.noztech.coppy.feature.home.domain.usecase.GetItemCountByGroupUseCase
import org.noztech.coppy.feature.home.presentation.SaveState

class GroupViewModel(
    private val createGroupUseCase: CreateGroupUseCase,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val getItemCountByGroupUseCase: GetItemCountByGroupUseCase
) : ViewModel() {

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = _saveState.asStateFlow()

    val groupsWithCount: StateFlow<List<Pair<VaultGroup, Long>>> =
        combine(
            getGroupsUseCase(),
            getItemCountByGroupUseCase()
        ) { groups, counts ->
            groups.map { group ->
                val count = counts.find { it.groupId == group.id }?.itemCount ?: 0L
                group to count
            }
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(5000),
                emptyList()
            )

    fun createGroup(name: String) {
        viewModelScope.launch {
            _saveState.value = SaveState.Loading
            try {
                createGroupUseCase(name)
                _saveState.value = SaveState.Success("Group created")
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "Unknown error")
            }
        }
    }
}