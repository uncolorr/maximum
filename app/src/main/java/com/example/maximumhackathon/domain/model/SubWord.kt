package com.example.maximumhackathon.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubWord(
    @SerialName("text") val text: String
)
