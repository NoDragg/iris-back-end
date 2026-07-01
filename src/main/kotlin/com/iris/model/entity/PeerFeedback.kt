package com.iris.model.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "peer_feedback")
data class PeerFeedback(
    @Id
    val id: String = java.util.UUID.randomUUID().toString(),

    @Column(name = "member_id", nullable = false)
    val memberId: String,

    @Column(name = "author_id")
    val authorId: String? = null,

    @Column(name = "author_name")
    val authorName: String? = null,

    @Column(nullable = false)
    var status: String,

    var strengths: String? = null,

    var improve: String? = null,

    val date: LocalDate = LocalDate.now(),

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
