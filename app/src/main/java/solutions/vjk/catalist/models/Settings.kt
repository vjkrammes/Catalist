package solutions.vjk.catalist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

private const val JSON_ID: String = "id"
private const val JSON_DELETE_ON_COMPLETE: String = "deleteOnComplete"

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "deleteOnComplete")
    val deleteOnComplete: Boolean = false
) {

    constructor(obj: JSONObject) : this(
        obj.getInt(JSON_ID),
        obj.getBoolean(JSON_DELETE_ON_COMPLETE)
    )

    fun toJson(): JSONObject {
        val ret = JSONObject()
        ret.put(JSON_ID, id)
        ret.put(JSON_DELETE_ON_COMPLETE, deleteOnComplete)
        return ret
    }
}
