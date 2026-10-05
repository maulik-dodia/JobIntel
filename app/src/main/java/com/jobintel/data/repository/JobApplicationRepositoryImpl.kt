package com.jobintel.data.repository

import com.jobintel.data.local.JobApplicationDao
import com.jobintel.data.local.JobApplicationEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class JobApplicationRepositoryImpl @Inject constructor(
    private val jobApplicationDao: JobApplicationDao,
) : JobApplicationRepository {
    override fun observeAll(): Flow<List<JobApplicationEntity>> = jobApplicationDao.observeAll()

    override fun observeById(id: Long): Flow<JobApplicationEntity?> =
        jobApplicationDao.observeById(id)

    override suspend fun insert(jobApplication: JobApplicationEntity): Long =
        jobApplicationDao.insert(jobApplication)

    override suspend fun update(jobApplication: JobApplicationEntity) {
        jobApplicationDao.update(jobApplication)
    }

    override suspend fun delete(jobApplication: JobApplicationEntity) {
        jobApplicationDao.delete(jobApplication)
    }
}