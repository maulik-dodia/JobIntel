package com.jobintel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
                val jobApplications by viewModel.jobApplications.collectAsStateWithLifecycle()
                val selectedJobApplication = jobApplications.firstOrNull {
                    it.id == selectedJobApplicationId
                }
                if (showAddJobApplication || isEditingJobApplication) {
                    AddJobApplicationScreen(
                        initialJobApplication = selectedJobApplication,
                        onSave = { jobApplication ->
                            val result = if (isEditingJobApplication) {
                                selectedJobApplication?.let {
                                    viewModel.updateJobApplication(jobApplication)
                                } ?: return@AddJobApplicationScreen false
                            } else {
                                viewModel.addJobApplication(jobApplication)
                            }
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
                } else {
                    JobApplicationScreen(
                        viewModel = viewModel,
                        onAddJobApplication = {
                            selectedJobApplicationId = null
                            isEditingJobApplication = false
                            showAddJobApplication = true
                        },
                        onEditJobApplication = { jobApplication ->
                            showAddJobApplication =  false
                            isEditingJobApplication = true
                            selectedJobApplicationId = jobApplication.id
                        }
                    )
                }
            }
        }
    }
}