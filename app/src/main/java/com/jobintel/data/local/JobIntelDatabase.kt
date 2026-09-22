package com.jobintel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [JobApplicationEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(JobApplicationTypeConverters::class)
abstract class JobIntelDatabase : RoomDatabase() {
    abstract fun jobApplicationDao(): JobApplicationDao
}