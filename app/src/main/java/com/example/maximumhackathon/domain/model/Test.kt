package com.example.maximumhackathon.domain.model

import java.io.Serializable

data class Test(
    val id: Int,
    val name: String,
    val status: TestStatus,
    val stats: String,
    val number: Int,
    val description: String,
    val dbReference: String,
    val user: String? = ""
): Serializable
