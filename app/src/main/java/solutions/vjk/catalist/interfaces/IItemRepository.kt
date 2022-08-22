package solutions.vjk.catalist.interfaces

import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.models.RepositoryResult

interface IItemRepository : IRepository<Item> {
    suspend fun getForList(listId: Int): List<Item>
    suspend fun getForCategory(categoryId: Int): List<Item>
    suspend fun getForAssignee(assigneeId: Int): List<Item>
    suspend fun read(name: String, categoryId: Int): Item?
    suspend fun assignmentCount(assigneeId: Int): Int
    suspend fun complete(item: Item): RepositoryResult
    suspend fun countForCategory(categoryId: Int): Int
    suspend fun removeAssignee(itemId: Int): RepositoryResult
}