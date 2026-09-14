package com.jobintel.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jobintel.domain.model.ApplicationStatus
import java.time.LocalDate

@Entity(tableName = "job_applications")
data class JobApplicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val companyName: String,
    val jobTitle: String,
    val dateApplied: LocalDate?,
    val country: String?,
    val status: ApplicationStatus,
    val createdAt: Long,
    val updatedAt: Long,
)