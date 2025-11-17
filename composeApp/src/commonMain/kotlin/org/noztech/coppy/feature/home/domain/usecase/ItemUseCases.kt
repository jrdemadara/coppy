package org.noztech.coppy.feature.home.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.noztech.GetItemCountByGroup
import org.noztech.VaultItem
import org.noztech.coppy.feature.home.domain.respository.ItemRepository

class CreateItemUseCase(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(
        groupId: Long?,
        title: String,
        value: String?,
        hidden: Boolean = false
    ): Long {

        return repository.createItem(groupId, title, value, hidden)
    }
}

class UpdateItemUseCase(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(
        id: Long,
        groupId: Long?,
        title: String,
        value: String?,
    ) {
        repository.updateItem(id, groupId, title, value)
    }
}

class ToggleItemVisibilityUseCase(
    private val repository: ItemRepository
){
    suspend operator fun invoke(
        id: Long,
    ) {
        repository.toggleItemVisibility(id)
    }
}

class DeleteItemUseCase(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteItem(id)
    }
}

class GetItemsUseCase(
    private val repository: ItemRepository
) {
    operator fun invoke(): Flow<List<VaultItem>> = repository.getItems()
}

class GetItemByIdUseCase(
    private val repository: ItemRepository
) {
    operator fun invoke(id: Long): VaultItem? {
        return repository.getItemById(id)
    }
}

class GetItemCountByGroupUseCase(
    private val repository: ItemRepository
) {
    operator fun invoke(): Flow<List<GetItemCountByGroup>> =
        repository.getItemCountByGroup()
}

class GetItemCountForGroupUseCase(
    private val repository: ItemRepository
) {
    operator fun invoke(groupId: Long): Flow<Long> =
        repository.getItemCountForGroup(groupId)
}