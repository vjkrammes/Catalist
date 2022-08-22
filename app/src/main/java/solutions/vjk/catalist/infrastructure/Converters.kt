package solutions.vjk.catalist.infrastructure

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun FromCalendar(value: Calendar?): Int? {
        return value?.let { it.toInt() }
    }

    @TypeConverter
    fun toCalendar(value: Int?): Calendar? {
        return value?.let { it.toCalendar() }
    }
}