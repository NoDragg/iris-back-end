package com.iris.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateMemberRequest(
    @field:NotBlank
    val name: String,
    val role: String? = null,
    val age: Int? = null,
    val location: String? = null,
    val rank: String? = null,
    val position: String? = null,
    val aim: Int? = null,
    val gameSense: Int? = null,
    val teamwork: Int? = null,
    val communication: Int? = null
)

data class UpdateMemberRequest(
    val name: String? = null,
    val role: String? = null,
    val age: Int? = null,
    val location: String? = null,
    val rank: String? = null,
    val position: String? = null,
    val avatarUrl: String? = null
)

data class UpdateStatsRequest(
    val aim: Int? = null,
    val gameSense: Int? = null,
    val teamwork: Int? = null,
    val communication: Int? = null
)
