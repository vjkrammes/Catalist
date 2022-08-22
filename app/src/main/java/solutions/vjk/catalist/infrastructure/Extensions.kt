package solutions.vjk.catalist.infrastructure

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

private const val yyyyFormat: String = "%04d"
private const val mmddFormat: String = "%02d"
private const val US_DOLLARS: String = "USD"
private const val DATE_SEPARATOR: Char = '/'

fun Calendar.toInt(): Int {
    val yyyy = this.get(Calendar.YEAR)
    val mm = this.get(Calendar.MONTH) + 1
    val dd = this.get(Calendar.DAY_OF_MONTH)
    return (yyyy * 10000) + (mm * 100) + dd
}

fun Int.toCalendar(): Calendar {
    val ret = Calendar.getInstance()
    val yyyy = this / 10000
    val mm = ((this % 10000) / 100) - 1
    val dd = this % 100
    ret.set(Calendar.YEAR, yyyy)
    ret.set(Calendar.MONTH, mm)
    ret.set(Calendar.DAY_OF_MONTH, dd)
    ret.set(Calendar.HOUR, 0)
    ret.set(Calendar.MINUTE, 0)
    ret.set(Calendar.SECOND, 0)
    ret.set(Calendar.MILLISECOND, 0)
    return ret
}

fun Int.toDateString(): String {
    val yyyy = this / 10000
    val mm = ((this % 10000) / 100)
    val dd = this % 100
    val sb = StringBuilder()
    sb.append(String.format(yyyyFormat, yyyy))
    sb.append(DATE_SEPARATOR)
    sb.append(String.format(mmddFormat, mm))
    sb.append(DATE_SEPARATOR)
    sb.append(String.format(mmddFormat, dd))
    return sb.toString()
}

fun Int.toCurrency(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance(US_DOLLARS)
    val value: Float = this.toFloat() / 100f
    return format.format(value)
}

fun LocalDate.toCalendar(): Calendar {
    val defaultZoneId = ZoneId.systemDefault()
    val date = Date.from(this.atStartOfDay(defaultZoneId).toInstant())
    val ret = Calendar.getInstance()
    ret.time = date
    return ret
}

fun Calendar.toLocalDate(): LocalDate {
    val yyyy = this.get(Calendar.YEAR)
    val mm = this.get(Calendar.MONTH) + 1
    val dd = this.get(Calendar.DAY_OF_MONTH)
    return LocalDate.of(yyyy, mm, dd)
}

fun Color.faded(transparency: Float): Color {
    return Color(this.red, this.green, this.blue, transparency)
}

fun Any?.discard() = Unit

fun String.filterText(charsToRemove: String): String {
    var ret = this
    for (char in charsToRemove) {
        ret = ret.replace(char.toString(), "")
    }
    return ret;
}

fun <T> MutableState<T>.asState(): State<T> = this

fun <T> MutableList<T>.asList(): List<T> = this