package solutions.vjk.catalist.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import solutions.vjk.catalist.infrastructure.Converters
import solutions.vjk.catalist.models.*
import solutions.vjk.catalist.repositories.dao.*

@Database(
    entities = [
        Settings::class,
        Category::class,
        Assignee::class,
        Item::class,
        ToDoList::class,
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CatalistDb : RoomDatabase() {
    abstract fun assigneeDao(): AssigneeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun itemDao(): ItemDao
    abstract fun toDoListDao(): ToDoListDao
    abstract fun settingsDao(): SettingsDao
}