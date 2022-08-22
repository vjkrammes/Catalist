package solutions.vjk.catalist.interfaces

import solutions.vjk.catalist.models.Category

interface ICategoryRepository : IRepository<Category> {
    suspend fun getForList(listId: Int): List<Category>
    suspend fun read(name: String): Category?
}