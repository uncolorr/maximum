package com.example.maximumhackathon.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubTranslate(
    @SerialName("tr") val tr: List<SubWord>
)
