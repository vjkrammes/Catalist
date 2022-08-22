package solutions.vjk.catalist.interfaces

import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.models.ToDoList

interface IListRepository : IRepository<ToDoList> {
    suspend fun read(name: String): ToDoList?
    suspend fun deleteList(id: Int): RepositoryResult
}