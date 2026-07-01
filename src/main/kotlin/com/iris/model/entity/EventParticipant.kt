package com.iris.model.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "event_participants")
@IdClass(EventParticipantId::class)
data class EventParticipant(
    @Id
    @Column(name = "event_id")
    val eventId: String,

    @Id
    @Column(name = "member_id")
    val memberId: String
)

data class EventParticipantId(
    val eventId: String = "",
    val memberId: String = ""
) : Serializable
