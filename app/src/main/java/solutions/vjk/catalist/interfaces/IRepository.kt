package solutions.vjk.catalist.interfaces

import solutions.vjk.catalist.models.RepositoryResult

interface IRepository<T : IIdModel> {
    suspend fun get(): List<T>
    suspend fun read(id: Int): T?
    suspend fun insert(model: T): RepositoryResult
    suspend fun update(model: T): RepositoryResult
    suspend fun delete(model: T): RepositoryResult
    suspend fun delete(id: Int): RepositoryResult
}