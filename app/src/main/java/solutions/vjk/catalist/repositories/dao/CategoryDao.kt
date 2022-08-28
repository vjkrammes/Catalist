package solutions.vjk.catalist.repositories.dao

import androidx.room.*
import solutions.vjk.catalist.models.Category

@Dao
interface CategoryDao {
    @Query("select * from categories")
    fun get(): List<Category>

    @Query("select * from categories where listId = :listId")
    fun getForList(listId: Int): List<Category>

    @Query("select * from categories where id = :id")
    fun read(id: Int): Category?

    @Query("select * from categories where listId = :listId and  name = :name")
    fun read(listId: Int, name: String): Category?

    @Insert
    fun insert(category: Category)

    @Update
    fun update(category: Category)

    @Delete
    fun delete(category: Category)
}