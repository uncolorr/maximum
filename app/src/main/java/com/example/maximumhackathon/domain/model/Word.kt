package com.example.maximumhackathon.domain.model

data class Word(
    val orderNumber: Int,
    val name: String,
    val translate: String = "***",
    val frequency: Long
)
