package com.example.maximumhackathon.domain.model

data class Lesson(
    val id: Int,
    val name: String,
    val number: Int,
    val status: LessonStatus,
    val description: String,
    val dbReference: String
)
