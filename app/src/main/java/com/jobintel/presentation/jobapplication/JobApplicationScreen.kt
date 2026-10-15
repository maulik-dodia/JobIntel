package com.jobintel.presentation.jobapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jobintel.R
import com.jobintel.data.local.JobApplicationEntity
import com.jobintel.domain.model.ApplicationStatus
import com.jobintel.ui.theme.JobIntelTheme
import java.time.LocalDate

@Composable
fun JobApplicationScreen(
    modifier: Modifier = Modifier,
    viewModel: JobApplicationViewModel = viewModel(),
    onAddJobApplication: () -> Unit,
    onEditJobApplication: (JobApplicationEntity) -> Unit,
    onDeleteJobApplication: (JobApplicationEntity) -> Unit
) {
    val jobApplications by viewModel.jobApplications.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    JobApplicationScreenContent(
        jobApplications = jobApplications,
        deleteState = deleteState,
        modifier = modifier,
        onAddJobApplication = onAddJobApplication,
        onEditJobApplication = onEditJobApplication,
        onDeleteJobApplication = onDeleteJobApplication,
        onResetDeleteState = viewModel::resetDeleteState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationScreenContent(
    modifier: Modifier = Modifier,
    jobApplications: List<JobApplicationEntity>,
    deleteState: DeleteJobApplicationState = DeleteJobApplicationState.Idle,
    onAddJobApplication: () -> Unit,
    onEditJobApplication: (JobApplicationEntity) -> Unit,
    onDeleteJobApplication: (JobApplicationEntity) -> Unit,
    onResetDeleteState: () -> Unit
) {
    var jobApplicationToDeleteId by rememberSaveable {
        mutableStateOf<Long?>(null)
    }
    val jobApplicationToDelete = jobApplications.firstOrNull {
        it.id == jobApplicationToDeleteId
    }
    val isDeleting = deleteState == DeleteJobApplicationState.Deleting
    val deleteError = deleteState == DeleteJobApplicationState.Error

    LaunchedEffect(deleteState) {
        when (deleteState) {
            DeleteJobApplicationState.Success -> {
                jobApplicationToDeleteId = null
                onResetDeleteState()
            }
            DeleteJobApplicationState.Error -> {
                // Keep the dialog open so the user can retry.
            }
            DeleteJobApplicationState.Idle,
            DeleteJobApplicationState.Deleting -> Unit
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.job_applications_title),
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddJobApplication,
            ) {
                Text(text = "+")
            }
        },
    ) { innerPadding ->
        if (jobApplications.isEmpty()) {
            JobApplicationEmptyState(
                modifier = Modifier.padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = jobApplications,
                    key = { it.id },
                ) { jobApplication ->
                    Column {
                        JobApplicationItem(
                            jobApplication = jobApplication,
                            onClick = { onEditJobApplication(jobApplication) }
                        )
                        TextButton(
                            onClick = {
                                jobApplicationToDeleteId = jobApplication.id
                                onResetDeleteState()
                            },
                            enabled = !isDeleting
                        ) {
                            Text(stringResource(R.string.delete_job_application))
                        }
                    }
                }
            }
        }
    }
    if (jobApplicationToDeleteId != null) {
        AlertDialog(
            onDismissRequest = {
                if (!isDeleting) {
                    jobApplicationToDeleteId = null
                    onResetDeleteState()
                }
            },
            title = {
                Text(stringResource(R.string.delete_job_application_title))
            },
            text = {
                Column {
                    Text(
                        text = stringResource(
                            R.string.delete_job_application_confirmation,
                            jobApplicationToDelete?.companyName.orEmpty()
                        )
                    )
                    if (deleteError) {
                        Text(
                            text = stringResource(R.string.delete_job_application_error),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isDeleting,
                    onClick = {
                        val application = jobApplicationToDelete
                            ?: return@TextButton
                        onDeleteJobApplication(application)
                    }
                ) {
                    Text(
                        if (isDeleting) {
                            stringResource(R.string.delete_job_application_progress)
                        } else {
                            stringResource(R.string.delete_job_application)
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isDeleting,
                    onClick = {
                        jobApplicationToDeleteId = null
                        onResetDeleteState()
                    }
                ) {
                    Text(stringResource(R.string.delete_job_application_cancel))
                }
            }
        )
    }
}

@Composable
fun JobApplicationItem(
    jobApplication: JobApplicationEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = jobApplication.companyName,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = jobApplication.jobTitle,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = jobApplication.country
                    ?: stringResource(R.string.job_application_country_unknown),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = jobApplication.status.displayName,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
fun JobApplicationEmptyState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.job_applications_empty),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JobApplicationScreenContentEmptyPreview() {
    JobIntelTheme {
        JobApplicationScreenContent(
            jobApplications = emptyList(),
            onAddJobApplication = {},
            onEditJobApplication = {},
            onDeleteJobApplication = {},
            onResetDeleteState = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JobApplicationScreenContentPreview() {
    JobIntelTheme {
        JobApplicationScreenContent(
            jobApplications = previewJobApplications,
            onAddJobApplication = {},
            onEditJobApplication = {},
            onDeleteJobApplication = {},
            onResetDeleteState = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JobApplicationItemPreview() {
    JobIntelTheme {
        JobApplicationItem(
            jobApplication = previewJobApplications.first(),
            onClick = {}
        )
    }
}

private val previewJobApplications = listOf(
    JobApplicationEntity(
        id = 1,
        companyName = "Acme Corp",
        jobTitle = "Android Developer",
        dateApplied = LocalDate.of(2026, 10, 4),
        country = "India",
        status = ApplicationStatus.APPLIED,
        createdAt = 1_000L,
        updatedAt = 2_000L,
    ),
    JobApplicationEntity(
        id = 2,
        companyName = "Globex",
        jobTitle = "Mobile Engineer",
        dateApplied = null,
        country = null,
        status = ApplicationStatus.SAVED,
        createdAt = 3_000L,
        updatedAt = 4_000L,
    )
)