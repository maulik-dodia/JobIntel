package com.jobintel.domain.model

enum class ApplicationStatus(val displayName: String) {
    SAVED("Saved"),
    APPLIED("Applied"),
    APPLICATION_VIEWED("Application Viewed"),
    SHORTLISTED("Shortlisted"),
    RECRUITER_CONTACTED("Recruiter Contacted"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    INTERVIEWING("Interviewing"),
    OFFER_RECEIVED("Offer Received"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn"),
    CLOSED("Closed"),
}