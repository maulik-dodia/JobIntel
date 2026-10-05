package com.jobintel.di

import com.jobintel.data.repository.JobApplicationRepository
import com.jobintel.data.repository.JobApplicationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindJobApplicationRepository(
        implementation: JobApplicationRepositoryImpl,
    ): JobApplicationRepository
}