package com.jobintel.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JobApplicationDao {
    @Query("SELECT * FROM job_applications ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM job_applications WHERE id = :id")
    fun observeById(id: Long): Flow<JobApplicationEntity?>

    @Insert
    suspend fun insert(jobApplication: JobApplicationEntity): Long

    @Update
    suspend fun update(jobApplication: JobApplicationEntity)

    @Delete
    suspend fun delete(jobApplication: JobApplicationEntity)
}