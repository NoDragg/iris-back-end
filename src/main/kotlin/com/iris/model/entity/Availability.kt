package com.iris.model.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "availabilities")
data class Availability(
    @Id
    val id: String = java.util.UUID.randomUUID().toString(),

    @Column(name = "member_id", nullable = false)
    val memberId: String,

    @Column(name = "week_key", nullable = false)
    val weekKey: LocalDate,

    @Column(name = "day_index", nullable = false)
    val dayIndex: Int,

    @Column(name = "start_hour", nullable = false)
    val startHour: Double,

    @Column(name = "end_hour", nullable = false)
    val endHour: Double
)
