package com.jobintel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                var showAddJobApplication by remember {
                    mutableStateOf(false)
                }
                if (showAddJobApplication) {
                    AddJobApplicationScreen(
                        onSave = { jobApplication ->
                            viewModel.addJobApplication(jobApplication)
                            showAddJobApplication = false
                        },
                        onCancel = {
                            showAddJobApplication = false
                        },
                    )
                } else {
                    JobApplicationScreen(
                        viewModel = viewModel,
                        onAddJobApplication = {
                            showAddJobApplication = true
                        },
                    )
                }
            }
        }
    }
}