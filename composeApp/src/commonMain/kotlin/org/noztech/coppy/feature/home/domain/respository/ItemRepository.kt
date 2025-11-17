package org.noztech.coppy.feature.home.domain.respository

import kotlinx.coroutines.flow.Flow
import org.noztech.GetItemCountByGroup
import org.noztech.VaultItem

interface ItemRepository {
    suspend fun createItem(
        groupId: Long?,
        title: String,
        value: String?,
        hidden: Boolean = false
    ): Long

    suspend fun updateItem(
        id: Long,
        groupId: Long?,
        title: String,
        value: String?
    )

    suspend fun toggleItemVisibility(
        id: Long,
    )

    suspend fun deleteItem(id: Long)

    fun getItems(): Flow<List<VaultItem>>
    fun getItemById(id: Long): VaultItem?
    suspend fun getItemsByGroup(groupId: Long): List<VaultItem>

    fun getItemCountByGroup(): Flow<List<GetItemCountByGroup>>
    fun getItemCountForGroup(groupId: Long): Flow<Long>
}