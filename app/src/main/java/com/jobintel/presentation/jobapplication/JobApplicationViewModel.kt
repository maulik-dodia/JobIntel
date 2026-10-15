package com.jobintel.presentation.jobapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobintel.data.local.JobApplicationEntity
import com.jobintel.data.repository.JobApplicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobApplicationViewModel @Inject constructor(
    private val jobApplicationRepository: JobApplicationRepository,
) : ViewModel() {

    private val _deleteState =
        MutableStateFlow<DeleteJobApplicationState>(
            DeleteJobApplicationState.Idle
        )

    val deleteState: StateFlow<DeleteJobApplicationState> =
        _deleteState.asStateFlow()

    fun resetDeleteState() {
        _deleteState.value = DeleteJobApplicationState.Idle
    }

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

    fun deleteJobApplication(jobApplication: JobApplicationEntity) {
        if (_deleteState.value == DeleteJobApplicationState.Deleting) {
            return
        }
        _deleteState.value = DeleteJobApplicationState.Idle
        viewModelScope.launch {
            _deleteState.value = DeleteJobApplicationState.Deleting
            _deleteState.value = try {
                jobApplicationRepository.delete(jobApplication)
                DeleteJobApplicationState.Success
            } catch (exception: Exception) {
                DeleteJobApplicationState.Error
            }
        }
    }
}

sealed interface DeleteJobApplicationState {
    data object Idle : DeleteJobApplicationState
    data object Deleting : DeleteJobApplicationState
    data object Success : DeleteJobApplicationState
    data object Error : DeleteJobApplicationState
}