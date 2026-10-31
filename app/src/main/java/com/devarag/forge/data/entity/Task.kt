package com.devarag.forge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val description: String = "",

    val completed: Boolean = false,

    // 1 = High, 2 = Medium, 3 = Low
    val priority: Int = 2,

    val dueDate: Long? = null,

    val createdAt: Long = System.currentTimeMillis()
)