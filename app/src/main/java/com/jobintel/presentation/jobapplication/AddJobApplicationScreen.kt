package com.jobintel.presentation.jobapplication

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jobintel.R
import com.jobintel.data.local.JobApplicationEntity
import com.jobintel.domain.model.ApplicationStatus
import com.jobintel.ui.theme.JobIntelTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJobApplicationScreen(
    modifier: Modifier = Modifier,
    onSave: suspend (JobApplicationEntity) -> Boolean,
    onCancel: () -> Unit
) {
    BackHandler {
        onCancel()
    }

    val coroutineScope = rememberCoroutineScope()

    var isSaving by rememberSaveable {
        mutableStateOf(false)
    }

    var companyName by rememberSaveable {
        mutableStateOf("")
    }

    var jobTitle by rememberSaveable {
        mutableStateOf("")
    }

    var country by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            R.string.add_job_application,
                        )
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(
                            R.string.company_name,
                        ),
                    )
                },
                singleLine = true
            )

            OutlinedTextField(
                value = jobTitle,
                onValueChange = { jobTitle = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(
                            R.string.job_title,
                        )
                    )
                },
                singleLine = true
            )

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(
                            R.string.country
                        )
                    )
                },
                singleLine = true
            )

            Button(
                onClick = {
                    isSaving = true
                    val jobApplication = JobApplicationEntity(
                        companyName = companyName.trim(),
                        jobTitle = jobTitle.trim(),
                        dateApplied = LocalDate.now(),
                        country = country.trim().ifBlank { null },
                        status = ApplicationStatus.APPLIED,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    coroutineScope.launch {
                        val saved = onSave(jobApplication)
                        if (!saved) {
                            isSaving = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving &&
                        companyName.isNotBlank() &&
                        jobTitle.isNotBlank()
            ) {
                Text(text = stringResource(R.string.save))
            }

            Button(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        R.string.cancel
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddJobApplicationScreenPreview() {
    JobIntelTheme {
        AddJobApplicationScreen(
            onSave = { true },
            onCancel = {}
        )
    }
}