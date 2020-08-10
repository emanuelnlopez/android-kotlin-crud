package ar.com.emanuellopez.android.crud.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriber_data_table")
data class Subscriber (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "email")
    var email: String
)