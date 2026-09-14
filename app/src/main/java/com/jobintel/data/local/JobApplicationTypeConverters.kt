package com.jobintel.data.local

import androidx.room.TypeConverter
import com.jobintel.domain.model.ApplicationStatus
import java.time.LocalDate

class JobApplicationTypeConverters {
    @TypeConverter
    fun localDateToString(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @TypeConverter
    fun applicationStatusToString(value: ApplicationStatus): String = value.name

    @TypeConverter
    fun stringToApplicationStatus(value: String): ApplicationStatus = ApplicationStatus.valueOf(value)
}