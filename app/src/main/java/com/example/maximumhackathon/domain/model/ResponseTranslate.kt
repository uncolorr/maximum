package com.example.maximumhackathon.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseTranslate(
    @SerialName("def") val def: List<SubTranslate>
)
