package com.devarag.forge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duty_leaves")
data class DutyLeave(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val eventName: String,
    val startDate: Long,
    val endDate: Long,

    val hoursCoveredCsv: String = "",
    val reason: String = "",
    val approvalStatus: String = "PENDING",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)