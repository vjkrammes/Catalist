package solutions.vjk.catalist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import solutions.vjk.catalist.EMPTY_JSON
import solutions.vjk.catalist.infrastructure.DEFAULT_DATE
import solutions.vjk.catalist.infrastructure.toCalendar
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.interfaces.IIdModel
import java.util.*

private const val JSON_ID: String = "id"
private const val JSON_LIST_ID: String = "listId"
private const val JSON_CATEGORY_ID: String = "categoryId"
private const val JSON_ASSIGNEE_ID: String = "assigneeId"
private const val JSON_NAME: String = "name"
private const val JSON_DUE_DATE: String = "dueDate"
private const val JSON_BUDGET: String = "budget"
private const val JSON_SPENT: String = "spent"
private const val JSON_IN_PROGRESS: String = "inProgress"
private const val JSON_COMPLETE: String = "complete"

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int = 0,
    @ColumnInfo(name = "listId")
    val listId: Int = 0,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int = 0,
    @ColumnInfo(name = "assigneeId")
    val assigneeId: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "dueDate")
    val dueDate: Calendar = DEFAULT_DATE,
    @ColumnInfo(name = "budget")
    val budget: Int = 0,
    @ColumnInfo(name = "spent")
    val spent: Int = 0,
    @ColumnInfo(name = "inProgress")
    val inProgress: Boolean = false,
    @ColumnInfo(name = "complete")
    val complete: Boolean = false,
) : IIdModel {
    constructor(obj: JSONObject) : this(
        obj.getInt(JSON_ID),
        obj.getInt(JSON_LIST_ID),
        obj.getInt(JSON_CATEGORY_ID),
        obj.getInt(JSON_ASSIGNEE_ID),
        obj.getString(JSON_NAME),
        obj.getInt(JSON_DUE_DATE).toCalendar(),
        obj.getInt(JSON_BUDGET),
        obj.getInt(JSON_SPENT),
        obj.getBoolean(JSON_IN_PROGRESS),
        obj.getBoolean(JSON_COMPLETE)
    )

    @Ignore
    var assignee: Assignee? = null

    override fun toString(): String = name

    fun hasDueDate(): Boolean = dueDate.get(Calendar.YEAR) > 1

    fun toJson(): JSONObject {
        val ret = JSONObject()
        ret.put(JSON_ID, id)
        ret.put(JSON_LIST_ID, listId)
        ret.put(JSON_CATEGORY_ID, categoryId)
        ret.put(JSON_ASSIGNEE_ID, assigneeId)
        ret.put(JSON_NAME, name)
        ret.put(JSON_DUE_DATE, dueDate.toInt())
        ret.put(JSON_BUDGET, budget)
        ret.put(JSON_SPENT, spent)
        ret.put(JSON_IN_PROGRESS, inProgress)
        ret.put(JSON_COMPLETE, complete)
        return ret
    }

    companion object {
        fun loadItems(json: String): List<Item> {
            val ret = ArrayList<Item>()
            if (json.isEmpty() || json == EMPTY_JSON) {
                return ret
            }
            val array = JSONTokener(json).nextValue() as JSONArray
            for (i in 0 until array.length()) {
                ret.add(Item(array.getJSONObject(i)))
            }
            return ret
        }
    }
}
