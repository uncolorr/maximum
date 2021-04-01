package com.example.maximumhackathon.domain.model

data class Test(
    val id: Int,
    val name: String,
    val status: TestStatus,
    val stats: String,
    val number: Int,
    val description: String
)
