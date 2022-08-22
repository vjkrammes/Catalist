package solutions.vjk.catalist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import solutions.vjk.catalist.EMPTY_JSON
import solutions.vjk.catalist.interfaces.IIdModel

private const val JSON_ID: String = "id"
private const val JSON_NAME: String = "name"
private const val JSON_IS_GLOBAL: String = "isGlobal"
private const val JSON_LIST_ID: String = "listId"

@Entity(tableName = "assignees")
data class Assignee(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "isGlobal")
    val isGlobal: Boolean = false,
    @ColumnInfo(name = "listId")
    val listId: Int = 0
) : IIdModel {
    constructor(obj: JSONObject) : this(
        obj.getInt(JSON_ID),
        obj.getString(JSON_NAME),
        obj.getBoolean(JSON_IS_GLOBAL),
        obj.getInt(JSON_LIST_ID)
    )

    override fun toString(): String = name

    fun toJson(): JSONObject {
        val ret = JSONObject()
        ret.put(JSON_ID, id)
        ret.put(JSON_NAME, name)
        ret.put(JSON_IS_GLOBAL, isGlobal)
        ret.put(JSON_LIST_ID, listId)
        return ret
    }

    companion object {
        fun loadAssignees(json: String): List<Assignee> {
            val ret = ArrayList<Assignee>()
            if (json.isEmpty() || json == EMPTY_JSON) {
                return ret
            }
            val array = JSONTokener(json).nextValue() as JSONArray
            for (i in 0 until array.length()) {
                ret.add(Assignee(array.getJSONObject(i)))
            }
            return ret
        }
    }
}