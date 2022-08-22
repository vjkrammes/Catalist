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
private const val JSON_LIST_ID: String = "listId"
private const val JSON_NAME: String = "name"
private const val JSON_ICON_ID: String = "iconId"
private const val JSON_BACKGROUND: String = "background"
private const val JSON_ARGB: String = "argb"
private const val JSON_IS_DEFAULT: String = "isDefault"

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int = 0,
    @ColumnInfo(name = "listId")
    val listId: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "iconId")
    val iconId: Int = 0,
    @ColumnInfo(name = "background")
    val background: String = "white",
    @ColumnInfo(name = "argb")
    val argb: Long = 0L,
    @ColumnInfo(name = "isDefault")
    val isDefault: Boolean = false
) : IIdModel {
    constructor(obj: JSONObject) : this(
        obj.getInt(JSON_ID),
        obj.getInt(JSON_LIST_ID),
        obj.getString(JSON_NAME),
        obj.getInt(JSON_ICON_ID),
        obj.getString(JSON_BACKGROUND),
        obj.getLong(JSON_ARGB),
        obj.getBoolean(JSON_IS_DEFAULT)
    )

    override fun toString(): String = name

    fun toJson(): JSONObject {
        val ret = JSONObject()
        ret.put(JSON_ID, id)
        ret.put(JSON_LIST_ID, listId)
        ret.put(JSON_NAME, name)
        ret.put(JSON_ICON_ID, iconId)
        ret.put(JSON_BACKGROUND, background)
        ret.put(JSON_ARGB, argb)
        ret.put(JSON_IS_DEFAULT, isDefault)
        return ret
    }

    companion object {

        fun loadCategories(json: String): List<Category> {
            val ret = ArrayList<Category>()
            if (json.isEmpty() || json == EMPTY_JSON) {
                return ret
            }
            val array = JSONTokener(json).nextValue() as JSONArray
            for (i in 0 until array.length()) {
                ret.add(Category(array.getJSONObject(i)))
            }
            return ret
        }
    }
}