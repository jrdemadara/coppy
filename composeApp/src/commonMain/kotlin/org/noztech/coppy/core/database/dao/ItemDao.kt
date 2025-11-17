package org.noztech.coppy.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import org.noztech.GetItemCountByGroup
import org.noztech.VaultItem
import org.noztech.VaultItemQueries

class ItemDao(private val queries: VaultItemQueries) {

    suspend fun insertItem(
        groupId: Long?,
        title: String,
        value: String?,
        hidden: Boolean = false
    ) {
        queries.insertItem(
            groupId,
            title,
            value,
            if (hidden) 1 else 0
        )
    }

    suspend fun updateItem(
        id: Long,
        groupId: Long?,
        title: String,
        value: String?,
    ) {
        queries.updateItem(
            groupId,
            title,
            value,
            id
        )
    }

    suspend fun deleteItem(id: Long) {
        queries.deleteItemById(id)
    }

    suspend fun deleteItemsByGroupId(groupId: Long) {
        queries.deleteItemsByGroupId(groupId)
    }

    fun getItems(): Flow<List<VaultItem>> =
        queries.getItems()
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun getItemsByGroup(groupId: Long) =
        queries.getItemsByGroupId(groupId).executeAsList()

    fun getItemById(id: Long) =
        queries.getItemById(id).executeAsOneOrNull()

    fun getHiddenItems() =
        queries.getHiddenItems().executeAsList()

    suspend fun toggleVisibility(id: Long) {
        queries.updateItemVisibility(id)
    }

    fun getItemCountByGroup(): Flow<List<GetItemCountByGroup>> =
        queries.getItemCountByGroup().asFlow().mapToList(Dispatchers.IO)

    fun getItemCountForGroup(groupId: Long): Flow<Long> =
        queries.getItemCountForGroup(groupId).asFlow().mapToOne(Dispatchers.IO)
}