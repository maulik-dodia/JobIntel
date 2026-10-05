package com.jobintel.di

import android.content.Context
import androidx.room.Room
import com.jobintel.data.local.JobApplicationDao
import com.jobintel.data.local.JobIntelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideJobIntelDatabase(
        @ApplicationContext context: Context,
    ): JobIntelDatabase =
        Room.databaseBuilder(context, JobIntelDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideJobApplicationDao(database: JobIntelDatabase): JobApplicationDao =
        database.jobApplicationDao()

    private const val DATABASE_NAME = "jobintel.db"
}