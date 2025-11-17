package org.noztech.coppy.feature.home.domain.respository

import kotlinx.coroutines.flow.Flow
import org.noztech.VaultGroup

interface GroupRepository {
    suspend fun createGroup(name: String)
    suspend fun updateGroup(id: Long, name: String)
    suspend fun deleteGroup(id: Long)
    suspend fun getGroupById(id: Long): VaultGroup?

    fun getGroups(): Flow<List<VaultGroup>>
}