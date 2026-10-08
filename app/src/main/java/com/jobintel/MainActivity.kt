package com.jobintel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
                if (showAddJobApplication) {
                    AddJobApplicationScreen(
                        onSave = { jobApplication ->
                            val result = viewModel.addJobApplication(jobApplication)
                            if (result.isSuccess) {
                                showAddJobApplication = false
                            }
                            result.isSuccess
                        },
                        onCancel = {
                            showAddJobApplication = false
                        }
                    )
                } else {
                    JobApplicationScreen(
                        viewModel = viewModel,
                        onAddJobApplication = {
                            showAddJobApplication = true
                        }
                    )
                }
            }
        }
    }
}