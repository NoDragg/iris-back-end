package com.iris.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "members")
data class Member(
    @Id
    val id: String = java.util.UUID.randomUUID().toString(),

    @Column(name = "team_id")
    val teamId: String? = null,

    @Column(nullable = false)
    var name: String,

    var role: String? = null,

    @Column(name = "avatar_url")
    var avatarUrl: String? = null,

    var age: Int? = null,

    var location: String? = null,

    @Column(name = "user_rank")
    var rank: String? = null,

    var position: String? = null,

    var aim: Int = 50,

    @Column(name = "game_sense")
    var gameSense: Int = 50,

    var teamwork: Int = 50,

    var communication: Int = 50,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
