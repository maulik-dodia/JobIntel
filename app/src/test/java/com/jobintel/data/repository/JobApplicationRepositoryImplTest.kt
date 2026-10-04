package com.jobintel.data.repository

import com.jobintel.data.local.JobApplicationDao
import com.jobintel.data.local.JobApplicationEntity
import com.jobintel.domain.model.ApplicationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import java.time.LocalDate

class JobApplicationRepositoryImplTest {
    @Test
    fun observeAll_delegatesToDaoWithoutTransformingFlow() {
        val jobApplicationDao = FakeJobApplicationDao()
        val repository = JobApplicationRepositoryImpl(jobApplicationDao)

        val result = repository.observeAll()

        assertSame(jobApplicationDao.allApplicationsFlow, result)
        assertEquals(1, jobApplicationDao.observeAllCallCount)
    }

    @Test
    fun observeById_delegatesToDaoWithoutTransformingFlow() {
        val jobApplicationDao = FakeJobApplicationDao()
        val repository = JobApplicationRepositoryImpl(jobApplicationDao)

        val result = repository.observeById(TEST_ID)

        assertSame(jobApplicationDao.applicationByIdFlow, result)
        assertEquals(TEST_ID, jobApplicationDao.observedId)
    }

    @Test
    fun insert_delegatesToDaoAndReturnsGeneratedId() = runBlocking {
        val jobApplicationDao = FakeJobApplicationDao(insertResult = GENERATED_ID)
        val repository = JobApplicationRepositoryImpl(jobApplicationDao)

        val result = repository.insert(TEST_JOB_APPLICATION)

        assertEquals(GENERATED_ID, result)
        assertSame(TEST_JOB_APPLICATION, jobApplicationDao.insertedJobApplication)
    }

    @Test
    fun update_delegatesToDao() = runBlocking {
        val jobApplicationDao = FakeJobApplicationDao()
        val repository = JobApplicationRepositoryImpl(jobApplicationDao)

        repository.update(TEST_JOB_APPLICATION)

        assertSame(TEST_JOB_APPLICATION, jobApplicationDao.updatedJobApplication)
    }

    @Test
    fun delete_delegatesToDao() = runBlocking {
        val jobApplicationDao = FakeJobApplicationDao()
        val repository = JobApplicationRepositoryImpl(jobApplicationDao)

        repository.delete(TEST_JOB_APPLICATION)

        assertSame(TEST_JOB_APPLICATION, jobApplicationDao.deletedJobApplication)
    }

    private class FakeJobApplicationDao(
        private val insertResult: Long = GENERATED_ID,
    ) : JobApplicationDao {
        val allApplicationsFlow: Flow<List<JobApplicationEntity>> = flowOf(listOf(TEST_JOB_APPLICATION))
        val applicationByIdFlow: Flow<JobApplicationEntity?> = flowOf(TEST_JOB_APPLICATION)
        var observeAllCallCount = 0
        var observedId: Long? = null
        var insertedJobApplication: JobApplicationEntity? = null
        var updatedJobApplication: JobApplicationEntity? = null
        var deletedJobApplication: JobApplicationEntity? = null

        override fun observeAll(): Flow<List<JobApplicationEntity>> {
            observeAllCallCount += 1
            return allApplicationsFlow
        }

        override fun observeById(id: Long): Flow<JobApplicationEntity?> {
            observedId = id
            return applicationByIdFlow
        }

        override suspend fun insert(jobApplication: JobApplicationEntity): Long {
            insertedJobApplication = jobApplication
            return insertResult
        }

        override suspend fun update(jobApplication: JobApplicationEntity) {
            updatedJobApplication = jobApplication
        }

        override suspend fun delete(jobApplication: JobApplicationEntity) {
            deletedJobApplication = jobApplication
        }
    }

    private companion object {
        const val TEST_ID = 1L
        const val GENERATED_ID = 42L

        val TEST_JOB_APPLICATION = JobApplicationEntity(
            id = TEST_ID,
            companyName = "Acme Corp",
            jobTitle = "Android Developer",
            dateApplied = LocalDate.of(2026, 10, 4),
            country = "India",
            status = ApplicationStatus.APPLIED,
            createdAt = 1_000L,
            updatedAt = 2_000L,
        )
    }
}