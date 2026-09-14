package com.jobintel.data.local

import com.jobintel.domain.model.ApplicationStatus
import java.time.LocalDate

data class JobApplicationEntity(
    val id: Long = 0,
    val companyName: String,
    val jobTitle: String,
    val dateApplied: LocalDate?,
    val country: String?,
    val status: ApplicationStatus,
    val createdAt: Long,
    val updatedAt: Long,
)