package solutions.vjk.catalist.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.interfaces.IAssigneeRepository
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.repositories.dao.AssigneeDao
import javax.inject.Inject

class AssigneeRepository @Inject constructor(private val dao: AssigneeDao) : IAssigneeRepository {
    override suspend fun get(): List<Assignee> {
        val ret = ArrayList<Assignee>()
        withContext(Dispatchers.IO) {
            try {
                val assignees = dao.get()
                ret.addAll(assignees)
            } catch (ex: Exception) {
                Log.e("DAO Failure/get", ex.message ?: "Unknown")
                Log.e("DAO Failure/get", ex.printStackTrace().toString())
            }
        }
        return ret.toList()
    }

    override suspend fun read(id: Int): Assignee? {
        var ret: Assignee? = null
        if (id <= 0) {
            return null
        }
        withContext(Dispatchers.IO) {
            try {
                ret = dao.read(id)
            } catch (ex: Exception) {
                Log.e("DAO Failure/readid", ex.message ?: "Unknown")
                Log.e("DAO Failure/readid", ex.printStackTrace().toString())
            }
        }
        return ret
    }

    override suspend fun read(name: String): Assignee? {
        var ret: Assignee? = null
        if (name.isEmpty()) {
            return null
        }
        withContext(Dispatchers.IO) {
            try {
                ret = dao.read(name)
            } catch (ex: Exception) {
                Log.e("DAO Failure/readname", ex.message ?: "Unknown")
                Log.e("DAO Failure/readname", ex.printStackTrace().toString())
            }
        }
        return ret
    }

    override suspend fun insert(model: Assignee): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                dao.insert(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun update(model: Assignee): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                dao.update(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun delete(model: Assignee): RepositoryResult {
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
        return withContext(Dispatchers.IO) {
            val assignee = read(id)
            return@withContext if (assignee == null) {
                RepositoryResult.notFound("No assignee with the id '$id' was found")
            } else {
                delete(assignee)
            }
        }
    }
}