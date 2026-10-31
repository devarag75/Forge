package com.devarag.forge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSession(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val durationMinutes: Int,

    val completedAt: Long = System.currentTimeMillis()

)