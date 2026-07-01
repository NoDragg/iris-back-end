package com.iris.model.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "events")
data class Event(
    @Id
    val id: String = java.util.UUID.randomUUID().toString(),

    @Column(name = "team_id")
    val teamId: String? = null,

    @Column(nullable = false)
    var title: String,

    var type: String? = null,

    var date: LocalDate,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalTime,

    @Column(name = "end_time", nullable = false)
    var endTime: LocalTime,

    var location: String? = null,

    @Column(name = "map_url")
    var mapUrl: String? = null,

    var notes: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
