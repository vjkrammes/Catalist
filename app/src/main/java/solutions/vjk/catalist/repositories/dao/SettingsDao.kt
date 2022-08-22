package solutions.vjk.catalist.repositories.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import solutions.vjk.catalist.models.Settings

@Dao
interface SettingsDao {
    @Query("Select * from settings where id = 1")
    fun read(): Settings

    @Query("Select count(*) from settings")
    fun getSettingsCount(): Int

    @Insert
    fun insert(Settings: Settings)

    @Update
    fun update(settings: Settings)
}