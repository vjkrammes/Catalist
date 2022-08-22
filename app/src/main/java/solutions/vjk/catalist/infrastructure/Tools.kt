package solutions.vjk.catalist.infrastructure

import java.text.SimpleDateFormat
import java.util.*

fun displayDate(date: Calendar, format: SimpleDateFormat, cutoffYear: Int? = 1): String {
    val cutoff = cutoffYear ?: 1
    if (date.get(Calendar.YEAR) < cutoff) {
        return ""
    }
    return format.format(date.time)
}

val DEFAULT_DATE: Calendar = 10101.toCalendar() // 01/01/0001

fun convertMoney(value: String): Int {
    if (value.isEmpty()) {
        return 0
    }
    return try {
        return value.toInt()
    } catch (_: Exception) {
        0
    }
}
