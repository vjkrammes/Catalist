package solutions.vjk.catalist.repositories.dao

import androidx.room.*
import solutions.vjk.catalist.models.Assignee

@Dao
interface AssigneeDao {
    @Query("select * from assignees")
    fun get(): List<Assignee>

    @Query("select * from assignees where listId = :listId")
    fun getForList(listId: Int): List<Assignee>

    @Query("select * from assignees where listId = :listId or isGlobal = 1")
    fun getForListWithGlobals(listId: Int): List<Assignee>

    @Query("select * from assignees where isGlobal = 1")
    fun getGlobalAssignees(): List<Assignee>

    @Query("select * from assignees where id = :id")
    fun read(id: Int): Assignee?

    @Query("select * from assignees where name = :name")
    fun read(name: String): Assignee?

    @Insert
    fun insert(assignee: Assignee)

    @Update
    fun update(assignee: Assignee)

    @Delete
    fun delete(assignee: Assignee)
}