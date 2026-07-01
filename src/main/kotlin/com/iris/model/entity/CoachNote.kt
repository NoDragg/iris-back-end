package com.iris.model.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "coach_notes")
data class CoachNote(
    @Id
    val id: String = java.util.UUID.randomUUID().toString(),

    @Column(name = "member_id", nullable = false)
    val memberId: String,

    @Column(nullable = false)
    var text: String,

    var status: String? = null,

    val date: LocalDate = LocalDate.now(),

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
