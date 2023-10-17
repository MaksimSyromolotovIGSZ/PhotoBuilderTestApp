package com.example.photobuilder.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "tag") val tag: String? = null
)
