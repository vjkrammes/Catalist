package solutions.vjk.catalist.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.interfaces.ICategoryRepository
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.repositories.dao.CategoryDao
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val dao: CategoryDao) : ICategoryRepository {
    override suspend fun get(): List<Category> {
        return withContext(Dispatchers.IO) {
            val ret = ArrayList<Category>()
            try {
                val categories = dao.get()
                ret.addAll(categories)
            } catch (ex: Exception) {
                Log.e("DAO Failure/get", ex.message ?: "Unknown")
                Log.e("DAO Failure/get", ex.printStackTrace().toString())
            }
            return@withContext ret.toList()
        }
    }

    override suspend fun getForList(listId: Int): List<Category> {
        return withContext(Dispatchers.IO) {
            val ret = ArrayList<Category>()
            try {
                val c = dao.getForList(listId)
                ret.addAll(c)
            } catch (ex: Exception) {
                Log.e("DAO Failure/get", ex.message ?: "Unknown")
                Log.e("DAO Failure/get", ex.printStackTrace().toString())
            }
            return@withContext ret.toList()
        }
    }

    override suspend fun read(id: Int): Category? {
        var ret: Category? = null
        if (id <= 0) {
            return null
        }
        withContext(Dispatchers.IO) {
            try {
                ret = dao.read(id)
            } catch (ex: Exception) {
                Log.e("DAO Failure/get", ex.message ?: "Unknown")
                Log.e("DAO Failure/get", ex.printStackTrace().toString())
            }
        }
        return ret
    }

    override suspend fun read(name: String): Category? {
        var ret: Category? = null
        if (name.isEmpty()) {
            return null
        }
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

    override suspend fun insert(model: Category): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                dao.insert(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun update(model: Category): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                dao.update(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun delete(model: Category): RepositoryResult {
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
            val category = read(id)
            return@withContext if (category == null) {
                RepositoryResult.notFound("No category with the id '$id' was found")
            } else {
                delete(category)
            }
        }
    }
}