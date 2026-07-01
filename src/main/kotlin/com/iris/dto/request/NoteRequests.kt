package com.iris.dto.request

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class AddNoteRequest(
    @field:NotBlank
    val text: String,
    val date: LocalDate? = null
)

data class AddCoachNoteRequest(
    @field:NotBlank
    val text: String,
    val status: String? = null,
    val date: LocalDate? = null
)

data class AddPeerFeedbackRequest(
    @field:NotBlank
    val status: String,
    val strengths: String? = null,
    val improve: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
    val date: LocalDate? = null
)

data class AddVODRequest(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val url: String,
    val date: LocalDate? = null
)
