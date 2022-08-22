package solutions.vjk.catalist.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.repositories.dao.AssigneeDao
import solutions.vjk.catalist.repositories.dao.ItemDao
import solutions.vjk.catalist.repositories.dao.SettingsDao
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val assigneeDao: AssigneeDao,
    private val settingsDao: SettingsDao
) : IItemRepository {

    private fun finish(item: Item): Item {
        val assignee: Assignee = assigneeDao.read(item.assigneeId) ?: return item
        item.assignee = assignee
        return item
    }

    private fun finish(items: List<Item>): List<Item> {
        val ret = ArrayList<Item>()
        items.forEach {
            ret.add(finish(it))
        }
        return ret
    }

    override suspend fun get(): List<Item> {
        val ret = ArrayList<Item>()
        withContext(Dispatchers.IO) {
            try {
                val i = itemDao.get()
                ret.addAll(i)
            } catch (ex: Exception) {
                Log.e("DAO Failure/get", ex.message ?: "Unknown")
                Log.e("DAO Failure/get", ex.printStackTrace().toString())
            }
        }
        return finish(ret)
    }

    override suspend fun getForList(listId: Int): List<Item> {
        val ret = ArrayList<Item>()
        withContext(Dispatchers.IO) {
            try {
                val i = itemDao.getForList(listId)
                ret.addAll(i)
            } catch (ex: Exception) {
                Log.e("DAO Failure/getForCategory", ex.message ?: "Unknown")
                Log.e("DAO Failure/getForCategory", ex.printStackTrace().toString())
            }
        }
        return finish(ret)
    }

    override suspend fun getForCategory(categoryId: Int): List<Item> {
        val ret = ArrayList<Item>()
        withContext(Dispatchers.IO) {
            try {
                val i = itemDao.getForCategory(categoryId)
                ret.addAll(i)
            } catch (ex: Exception) {
                Log.e("DAO Failure/getForCategory", ex.message ?: "Unknown")
                Log.e("DAO Failure/getForCategory", ex.printStackTrace().toString())
            }
        }
        return finish(ret)
    }

    override suspend fun getForAssignee(assigneeId: Int): List<Item> {
        val ret = ArrayList<Item>()
        withContext(Dispatchers.IO) {
            try {
                val i = itemDao.getForAssignee(assigneeId)
                ret.addAll(i)
            } catch (ex: Exception) {
                Log.e("DAO Failure/getForAssignee", ex.message ?: "Unknown")
                Log.e("DAO Failure/getForAssignee", ex.printStackTrace().toString())
            }
        }
        return finish(ret)
    }

    override suspend fun read(id: Int): Item? {
        var ret: Item? = null
        withContext(Dispatchers.IO) {
            try {
                ret = itemDao.read(id)
            } catch (ex: Exception) {
                Log.e("DAO Failure/read", ex.message ?: "Unknown")
                Log.e("DAO Failure/read", ex.printStackTrace().toString())
            }
        }
        return if (ret == null) null else finish(ret!!)
    }

    override suspend fun read(name: String, categoryId: Int): Item? {
        var ret: Item? = null
        withContext(Dispatchers.IO) {
            try {
                ret = itemDao.read(name, categoryId)
            } catch (ex: Exception) {
                Log.e("DAO Failure/readname", ex.message ?: "Unknown")
                Log.e("DAO Failure/readname", ex.printStackTrace().toString())
            }
        }
        return if (ret == null) null else finish(ret!!)
    }

    override suspend fun insert(model: Item): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                itemDao.insert(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun update(model: Item): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                itemDao.update(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun delete(model: Item): RepositoryResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                itemDao.delete(model)
                RepositoryResult.success()
            } catch (ex: Exception) {
                RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun delete(id: Int): RepositoryResult {
        val model = read(id)
        return if (model == null) {
            RepositoryResult.notFound("No item with the id '$id' was found")
        } else {
            delete(model)
        }
    }

    override suspend fun assignmentCount(assigneeId: Int): Int {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                itemDao.assignmentCount(assigneeId)
            } catch (ex: Exception) {
                Log.e("DAO Failure/ac", ex.message ?: "Unknown")
                Log.e("DAO Failure/ac", ex.printStackTrace().toString())
                0
            }
        }
    }

    override suspend fun complete(item: Item): RepositoryResult {
        val updatedItem = item.copy(complete = true)
        return withContext(Dispatchers.IO) {
            try {
                val settings = settingsDao.read()
                if (settings.deleteOnComplete) {
                    itemDao.delete(item)
                } else {
                    itemDao.update(updatedItem)
                }
                return@withContext RepositoryResult.success()
            } catch (ex: Exception) {
                return@withContext RepositoryResult.fromException(ex)
            }
        }
    }

    override suspend fun countForCategory(categoryId: Int): Int =
        itemDao.countForCategory(categoryId)

    override suspend fun removeAssignee(itemId: Int): RepositoryResult {
        return withContext(Dispatchers.IO) {
            try {
                val item = itemDao.read(itemId)
                if (item == null) {
                    return@withContext RepositoryResult.notFound("No item with the id '${itemId}' was found")
                } else {
                    val newItem = item.copy(assigneeId = 0)
                    return@withContext update(newItem)
                }
            } catch (ex: Exception) {
                return@withContext RepositoryResult.fromException(ex)
            }
        }
    }
}