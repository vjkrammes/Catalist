package solutions.vjk.catalist.repositories.dao

import androidx.room.*
import solutions.vjk.catalist.models.ToDoList

@Dao
interface ToDoListDao {
    @Query("select * from lists")
    fun get(): List<ToDoList>

    @Query("select * from lists where id = :id")
    fun read(id: Int): ToDoList?

    @Query("select * from lists where name = :name")
    fun read(name: String): ToDoList?

    @Insert
    fun insert(toDoList: ToDoList)

    @Update
    fun update(toDoList: ToDoList)

    @Delete
    fun delete(toDoList: ToDoList)

    @Query("delete from items where listId = :listId")
    fun deleteItemsForList(listId: Int)

    @Query("delete from categories where listId = :listId")
    fun deleteCategoriesForList(listId: Int)

    @Query("delete from lists where id = :listId")
    fun deleteList(listId: Int)

    @Transaction
    fun deleteListById(listId: Int) {
        deleteItemsForList(listId)
        deleteCategoriesForList(listId)
        deleteList(listId)
    }
}