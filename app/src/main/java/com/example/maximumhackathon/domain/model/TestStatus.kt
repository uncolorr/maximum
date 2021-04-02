package com.example.maximumhackathon.domain.model

enum class TestStatus(val code: String) {
    PENDING("pending"),
    COMPLETED("complete"),
    BLOCKED("blocked");

    companion object {
        fun valueByCode(code: String): TestStatus = code.let {
            values().find { it.code == code } ?: BLOCKED
        }
    }
}
