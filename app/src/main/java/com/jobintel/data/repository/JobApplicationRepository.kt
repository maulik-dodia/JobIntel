package com.jobintel.data.repository

import com.jobintel.data.local.JobApplicationEntity
import kotlinx.coroutines.flow.Flow

interface JobApplicationRepository {
    fun observeAll(): Flow<List<JobApplicationEntity>>

    fun observeById(id: Long): Flow<JobApplicationEntity?>

    suspend fun insert(jobApplication: JobApplicationEntity): Long

    suspend fun update(jobApplication: JobApplicationEntity)

    suspend fun delete(jobApplication: JobApplicationEntity)
}