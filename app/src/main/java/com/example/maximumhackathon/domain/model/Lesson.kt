package com.example.maximumhackathon.domain.model

import java.io.Serializable

data class Lesson(
    val id: Int,
    val name: String,
    val number: Int,
    var status: LessonStatus,
    val description: String,
    val dbReference: String,
    val user: String? = ""
): Serializable
