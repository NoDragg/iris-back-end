package com.iris.dto.response

import com.iris.model.entity.Event
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class EventResponse(
    val id: String,
    val title: String,
    val type: String?,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val location: String?,
    val mapUrl: String?,
    val notes: String?,
    val participants: List<ParticipantResponse>,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(e: Event, participants: List<ParticipantResponse>) = EventResponse(
            id = e.id,
            title = e.title,
            type = e.type,
            date = e.date,
            startTime = e.startTime,
            endTime = e.endTime,
            location = e.location,
            mapUrl = e.mapUrl,
            notes = e.notes,
            participants = participants,
            createdAt = e.createdAt
        )
    }
}

data class ParticipantResponse(
    val id: String,
    val name: String
)
