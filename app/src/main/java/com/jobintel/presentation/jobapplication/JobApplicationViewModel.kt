package com.jobintel.presentation.jobapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobintel.data.local.JobApplicationEntity
import com.jobintel.data.repository.JobApplicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JobApplicationViewModel @Inject constructor(
    jobApplicationRepository: JobApplicationRepository,
) : ViewModel() {

    val jobApplications: StateFlow<List<JobApplicationEntity>> =
        jobApplicationRepository.observeAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = emptyList(),
        )
}