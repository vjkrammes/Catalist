package solutions.vjk.catalist.repositories.dao

import androidx.room.*
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item

@Dao
interface ItemDao {
    @Query("select * from items")
    fun get(): List<Item>

    @Query("select * from items where listId = :listId")
    fun getForList(listId: Int): List<Item>

    @Query("select * from items where categoryId = :categoryId")
    fun getForCategory(categoryId: Int): List<Item>

    @Query("select * from items where assigneeId = :assigneeId")
    fun getForAssignee(assigneeId: Int): List<Item>

    @Query("select * from items where id = :id")
    fun read(id: Int): Item?

    @Query("select * from items where name = :name and categoryId = :categoryId")
    fun read(name: String, categoryId: Int): Item?

    @Query("select count(*) from items where assigneeId = :assigneeId")
    fun assignmentCount(assigneeId: Int) : Int

    @Query("select count(*) from items where categoryId = :categoryId")
    fun countForCategory(categoryId: Int): Int

    @Query("select count(*) from items where listId = :listId and dueDate > 10101 and dueDate < :dueDate and complete = 0")
    fun itemCountByDueDate(listId: Int, dueDate: Int): Int

    @Query("select * from items where listId = :listId and dueDate > 10101 and dueDate < :dueDate and complete = 0")
    fun itemsByDueDate(listId: Int, dueDate: Int): List<Item>

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)
}