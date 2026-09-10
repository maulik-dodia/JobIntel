# JobIntel

## Project Goal

JobIntel is an offline-first Android application for managing job applications and using on-device AI to extract job information and generate job-search insights.

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Repository pattern
- Room
- WorkManager
- Google Sheets API
- ML Kit Text Recognition
- Gemma 4 E2B IT for on-device AI
- GitHub Actions
- Android Lint

## Architecture Principles

- Room is the local source of truth.
- Google Sheets is used for synchronization, not the primary database.
- UI must not directly access Room or Google Sheets.
- Use Repository interfaces between data sources and the rest of the application.
- Deterministic calculations must be performed by Kotlin code, not the LLM.
- The LLM should interpret data and generate natural-language insights.
- AI-generated structured data must be validated before persistence.
- Extracted job information must be shown to the user for review/editing before saving.

## MVP Scope

1. Job application CRUD
2. Room local database
3. Google Sheets synchronization
4. Screenshot → OCR → AI extraction
5. User review/edit
6. Deterministic job-search analytics
7. AI-generated job-search insights

## Explicitly Out of Scope for MVP

- LinkedIn scraping
- Automatic job searching
- WhatsApp integration
- Resume generation
- Cover-letter generation
- Cloud backend
- Multi-user support
- Full AI chatbot

## Development Rules

- Do not introduce a new library without explaining why it is needed.
- Prefer Android/Jetpack solutions when appropriate.
- Keep implementations simple and maintainable.
- Do not make large unrelated changes.
- Write tests for important business logic.
- Run build, tests, and lint after meaningful changes.
- Never modify architecture without discussing the reason first.