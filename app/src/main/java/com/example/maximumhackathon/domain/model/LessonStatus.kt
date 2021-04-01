package com.example.maximumhackathon.domain.model

enum class LessonStatus(val code: String) {
    PENDING("pending"),
    COMPLETED("complete"),
    BLOCKED("blocked");

    companion object {
        fun valueByCode(code: String): LessonStatus = code.let {
            values().find { it.code == code } ?: LessonStatus.BLOCKED
        }
    }
}