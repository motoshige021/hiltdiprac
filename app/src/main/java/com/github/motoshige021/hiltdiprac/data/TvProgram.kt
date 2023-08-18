package com.github.motoshige021.hiltdiprac.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tvprograms")
data class TvProgram constructor(
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "completed") var isCompleted: Boolean,
    @PrimaryKey @ColumnInfo(name = "eventid") var id: String = UUID.randomUUID().toString()
    ) {

    val titleForList: String
    get() = if (title.isNotEmpty()) title else description

    val isActive : Boolean
    get() = !isCompleted
}