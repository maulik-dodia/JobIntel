package com.jobintel.presentation.jobapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobintel.data.local.JobApplicationEntity
import com.jobintel.data.repository.JobApplicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JobApplicationViewModel @Inject constructor(
    private val jobApplicationRepository: JobApplicationRepository,
) : ViewModel() {

    private val jobApplicationsState: StateFlow<List<JobApplicationEntity>?> =
        jobApplicationRepository.observeAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = null
        )
    val jobApplicationsWithLoading: StateFlow<List<JobApplicationEntity>?> =
        jobApplicationsState

    val jobApplications: StateFlow<List<JobApplicationEntity>> =
        jobApplicationsState
            .map { it.orEmpty() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = emptyList()
            )

    suspend fun addJobApplication(
        jobApplication: JobApplicationEntity
    ): Result<Long> {
        return runCatching {
            jobApplicationRepository.insert(jobApplication)
        }
    }

    suspend fun updateJobApplication(
        jobApplication: JobApplicationEntity
    ): Result<Unit> {
        return runCatching {
            jobApplicationRepository.update(jobApplication)
        }
    }

    suspend fun deleteJobApplication(
        jobApplication: JobApplicationEntity
    ): Result<Unit> {
        return runCatching {
            jobApplicationRepository.delete(jobApplication)
        }
    }
}