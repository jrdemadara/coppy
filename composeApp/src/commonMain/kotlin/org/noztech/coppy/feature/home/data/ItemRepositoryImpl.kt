package org.noztech.coppy.feature.home.data

import kotlinx.coroutines.flow.Flow
import org.noztech.GetItemCountByGroup
import org.noztech.VaultItem
import org.noztech.coppy.core.database.dao.ItemDao
import org.noztech.coppy.feature.home.domain.respository.ItemRepository

class ItemRepositoryImpl(
    private val dao: ItemDao
) : ItemRepository {

    override suspend fun createItem(
        groupId: Long?,
        title: String,
        value: String?,
        hidden: Boolean
    ): Long {
        dao.insertItem(
            groupId = groupId,
            title = title,
            value = value,
            hidden = hidden
        )
        // If you want the new ID, query for it (SQLDelight doesn’t return IDs directly)
        return dao.getItemsByGroup(groupId ?: 0).lastOrNull()?.id ?: -1
    }

    override suspend fun updateItem(
        id: Long,
        groupId: Long?,
        title: String,
        value: String?
    ) {
        dao.updateItem(id, groupId, title, value)
    }

    override suspend fun toggleItemVisibility(id: Long) {
        dao.toggleVisibility(id)
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteItem(id)
    }

    override fun getItems(): Flow<List<VaultItem>> {
        return dao.getItems()
    }

    override fun getItemById(id: Long): VaultItem? =
        dao.getItemById(id)

    override suspend fun getItemsByGroup(groupId: Long): List<VaultItem> =
        dao.getItemsByGroup(groupId)


    override fun getItemCountByGroup(): Flow<List<GetItemCountByGroup>> =
        dao.getItemCountByGroup()

    override fun getItemCountForGroup(groupId: Long): Flow<Long> =
        dao.getItemCountForGroup(groupId)


}