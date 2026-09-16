package com.jobintel.domain.model

enum class ApplicationStatus(
    val displayName: String,
    val dbValue: String,
) {
    SAVED("Saved", "saved"),
    APPLIED("Applied", "applied"),
    APPLICATION_VIEWED("Application Viewed", "application_viewed"),
    SHORTLISTED("Shortlisted", "shortlisted"),
    RECRUITER_CONTACTED("Recruiter Contacted", "recruiter_contacted"),
    INTERVIEW_SCHEDULED("Interview Scheduled", "interview_scheduled"),
    INTERVIEWING("Interviewing", "interviewing"),
    OFFER_RECEIVED("Offer Received", "offer_received"),
    ACCEPTED("Accepted", "accepted"),
    REJECTED("Rejected", "rejected"),
    WITHDRAWN("Withdrawn", "withdrawn"),
    CLOSED("Closed", "closed");

    companion object {
        fun fromDbValue(value: String): ApplicationStatus =
            requireNotNull(entries.firstOrNull { it.dbValue == value }) {
                "Unknown application status database value: $value"
            }
    }
}