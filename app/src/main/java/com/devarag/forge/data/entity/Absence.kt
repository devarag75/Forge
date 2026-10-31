package com.devarag.forge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "absences")
data class Absence(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: Long,

    // Empty means full day; "1,2,3" means selected periods
    val periodNumbersCsv: String = "",

    val reason: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)