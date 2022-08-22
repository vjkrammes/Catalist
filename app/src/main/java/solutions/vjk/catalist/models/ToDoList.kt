package solutions.vjk.catalist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import solutions.vjk.catalist.EMPTY_JSON
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.interfaces.IIdModel
import java.util.*

private const val JSON_ID: String = "id"
private const val JSON_NAME: String = "name"
private const val JSON_DATE_CREATED: String = "dateCreated"

@Entity(tableName = "lists")
data class ToDoList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "dateCreated")
    val dateCreated: Int = Calendar.getInstance().toInt()
) : IIdModel {
    constructor(obj: JSONObject) : this(
        obj.getInt(JSON_ID),
        obj.getString(JSON_NAME),
        obj.getInt(JSON_DATE_CREATED)
    )

    override fun toString(): String = name

    fun toJson(): JSONObject {
        val ret = JSONObject()
        ret.put(JSON_ID, id)
        ret.put(JSON_NAME, name)
        ret.put(JSON_DATE_CREATED, dateCreated)
        return ret
    }

    companion object {
        fun loadLists(json: String): List<ToDoList> {
            val ret = ArrayList<ToDoList>()
            if (json.isEmpty() || json == EMPTY_JSON) {
                return ret
            }
            val array = JSONTokener(json).nextValue() as JSONArray
            for (i in 0 until array.length()) {
                ret.add(ToDoList(array.getJSONObject(i)))
            }
            return ret
        }
    }
}
