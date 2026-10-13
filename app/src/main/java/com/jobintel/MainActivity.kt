package com.jobintel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jobintel.presentation.jobapplication.AddJobApplicationScreen
import com.jobintel.presentation.jobapplication.JobApplicationScreen
import com.jobintel.presentation.jobapplication.JobApplicationViewModel
import com.jobintel.ui.theme.JobIntelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobIntelTheme {
                val viewModel: JobApplicationViewModel = viewModel()
                var showAddJobApplication by rememberSaveable {
                    mutableStateOf(false)
                }
                var isEditingJobApplication by rememberSaveable {
                    mutableStateOf(false)
                }
                var selectedJobApplicationId by rememberSaveable {

                    mutableStateOf<Long?>(null)
                }
                val loadedJobApplications by
                viewModel.jobApplicationsWithLoading.collectAsStateWithLifecycle()

                val jobApplications = loadedJobApplications.orEmpty()
                val hasLoadedJobApplications = loadedJobApplications != null

                val selectedJobApplication = jobApplications.firstOrNull {
                    it.id == selectedJobApplicationId
                }
                if (showAddJobApplication) {
                    AddJobApplicationScreen(
                        onSave = { jobApplication ->
                            val result = viewModel.addJobApplication(jobApplication)
                            if (result.isSuccess) {
                                showAddJobApplication = false
                                isEditingJobApplication = false
                                selectedJobApplicationId = null
                            }
                            result.isSuccess
                        },
                        onCancel = {
                            showAddJobApplication = false
                            isEditingJobApplication = false
                            selectedJobApplicationId = null
                        }
                    )
                } else if (isEditingJobApplication) {
                    when {
                        selectedJobApplication != null -> {
                            AddJobApplicationScreen(
                                initialJobApplication = selectedJobApplication,
                                onSave = { jobApplication ->
                                    val result =
                                        viewModel.updateJobApplication(jobApplication)
                                    if (result.isSuccess) {
                                        showAddJobApplication = false
                                        isEditingJobApplication = false
                                        selectedJobApplicationId = null
                                    }
                                    result.isSuccess
                                },
                                onCancel = {
                                    showAddJobApplication = false
                                    isEditingJobApplication = false
                                    selectedJobApplicationId = null
                                }
                            )
                        }

                        !hasLoadedJobApplications -> {
                            JobApplicationLoadingIndicator()
                        }

                        else -> {
                            LaunchedEffect(selectedJobApplicationId) {
                                isEditingJobApplication = false
                                selectedJobApplicationId = null
                            }
                            JobApplicationLoadingIndicator()
                        }
                    }
                } else {
                    JobApplicationScreen(
                        viewModel = viewModel,
                        onAddJobApplication = {
                            selectedJobApplicationId = null
                            isEditingJobApplication = false
                            showAddJobApplication = true
                        },
                        onEditJobApplication = { jobApplication ->
                            showAddJobApplication = false
                            isEditingJobApplication = true
                            selectedJobApplicationId = jobApplication.id
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun JobApplicationLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}