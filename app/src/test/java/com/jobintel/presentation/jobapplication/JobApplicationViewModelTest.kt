package com.jobintel.presentation.jobapplication

import com.jobintel.data.local.JobApplicationEntity
import com.jobintel.data.repository.JobApplicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class JobApplicationViewModelTest {
    @Test

    fun jobApplications_startsEmptyAndUsesRepositoryFlow() {
        val repository = FakeJobApplicationRepository()
        val viewModel = JobApplicationViewModel(repository)

        assertEquals(emptyList<JobApplicationEntity>(), viewModel.jobApplications.value)
        assertEquals(1, repository.observeAllCallCount)
    }

    private class FakeJobApplicationRepository : JobApplicationRepository {
        val jobApplications = MutableStateFlow<List<JobApplicationEntity>>(emptyList())
        var observeAllCallCount = 0

        override fun observeAll(): Flow<List<JobApplicationEntity>> {
            observeAllCallCount += 1
            return jobApplications
        }

        override fun observeById(id: Long): Flow<JobApplicationEntity?> = MutableStateFlow(null)

        override suspend fun insert(jobApplication: JobApplicationEntity): Long = 0L

        override suspend fun update(jobApplication: JobApplicationEntity) = Unit

        override suspend fun delete(jobApplication: JobApplicationEntity) = Unit
    }
}