package com.iris.dto.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalTime

data class CreateEventRequest(
    @field:NotBlank
    val title: String,
    val type: String? = null,
    @field:NotNull
    val date: LocalDate,
    @field:NotNull
    val startTime: LocalTime,
    @field:NotNull
    val endTime: LocalTime,
    val location: String? = null,
    @JsonAlias("map")
    val mapUrl: String? = null,
    val notes: String? = null,
    @JsonAlias("participants")
    val participantIds: List<String>? = null
)

data class UpdateEventRequest(
    val title: String? = null,
    val type: String? = null,
    val date: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val location: String? = null,
    @JsonAlias("map")
    val mapUrl: String? = null,
    val notes: String? = null,
    @JsonAlias("participants")
    val participantIds: List<String>? = null
)

data class AddParticipantRequest(
    val memberId: String
)
