package solutions.vjk.catalist.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.interfaces.IListRepository
import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.repositories.dao.ToDoListDao
import java.util.*
import javax.inject.Inject

class ToDoListRepository @Inject constructor(private val dao: ToDoListDao) : IListRepository {

    override suspend fun get(): List<ToDoList> {
        val ret = ArrayList<ToDoList>()
        withContext(Dispatchers.IO) {
            try {
                val l = dao.get()
                ret.addAll(l)
            } catch (ex: Exception) {
                Log.e("DAO Failure/get", ex.message ?: "Unknown")
                Log.e("DAO Failure/get", ex.printStackTrace().toString())
            }
        }
        return ret.toList()
    }

    override suspend fun read(id: Int): ToDoList? {
        if (id <= 0) {
            return null
        }
        var ret: ToDoList? = null
        withContext(Dispatchers.IO) {
            try {
                ret = dao.read(id)
            } catch (ex: Exception) {
                Log.e("DAO Failure/read", ex.message ?: "Unknown")
                Log.e("DAO Failure/read", ex.printStackTrace().toString())
            }
        }
        return ret
    }

    override suspend fun read(name: String): ToDoList? {
        if (name.isEmpty()) {
            return null
        }
        var ret: ToDoList? = null
        withContext(Dispatchers.IO) {
            try {
                ret = dao.read(name)
            } catch (ex: Exception) {
                Log.e("DAO Failure/get", ex.message ?: "Unknown")
                Log.e("DAO Failure/get", ex.printStackTrace().toString())
            }
        }
        return ret
    }

    override suspend fun insert(model: ToDoList): RepositoryResult {
        if (model.name.isEmpty()) {
            return RepositoryResult.invalid("Name is required")
        }
        return withContext(Dispatchers.IO) {
            return@withContext try {
                dao.insert(model.copy(dateCreated = Calendar.getInstance().toInt()))
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun update(model: ToDoList): RepositoryResult {
        if (model.id <= 0) {
            return RepositoryResult.notFound("No item with the id '${model.id}' was found")
        }
        withContext(Dispatchers.IO) {
            val existing = read(model.id)
            if (existing == null) {
                return@withContext RepositoryResult.notFound("No item with the id '${model.id}' was found")
            } else {
                try {
                    dao.update(existing.copy(name = model.name))
                    return@withContext RepositoryResult.success()
                } catch (ex: Exception) {
                    return@withContext RepositoryResult.fromException(ex)
                }
            }
        }
        return RepositoryResult.success()
    }

    override suspend fun delete(model: ToDoList): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                dao.delete(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun delete(id: Int): RepositoryResult {
        val model = read(id)
        return if (model == null) {
            RepositoryResult.notFound("No list with the id '$id' was found")
        } else {
            delete(model)
        }
    }

    override suspend fun deleteList(id: Int): RepositoryResult {
        return try {
            dao.deleteListById(id)
            RepositoryResult.success()
        } catch (ex: Exception) {
            RepositoryResult.fromException(ex)
        }
    }
}