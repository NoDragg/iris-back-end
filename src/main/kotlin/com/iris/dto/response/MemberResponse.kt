package com.iris.dto.response

import com.iris.model.entity.*
import java.time.LocalDateTime

data class MemberResponse(
    val id: String,
    val name: String,
    val role: String?,
    val avatarUrl: String?,
    val age: Int?,
    val location: String?,
    val rank: String?,
    val position: String?,
    val stats: StatsResponse,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(m: Member) = MemberResponse(
            id = m.id,
            name = m.name,
            role = m.role,
            avatarUrl = m.avatarUrl,
            age = m.age,
            location = m.location,
            rank = m.rank,
            position = m.position,
            stats = StatsResponse(m.aim, m.gameSense, m.teamwork, m.communication),
            createdAt = m.createdAt
        )
    }
}

data class StatsResponse(
    val aim: Int,
    val gameSense: Int,
    val teamwork: Int,
    val communication: Int
)

data class LeaderNoteResponse(
    val id: String,
    val memberId: String,
    val text: String,
    val date: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(n: LeaderNote) = LeaderNoteResponse(
            id = n.id,
            memberId = n.memberId,
            text = n.text,
            date = n.date.toString(),
            createdAt = n.createdAt
        )
    }
}

data class CoachNoteResponse(
    val id: String,
    val memberId: String,
    val text: String,
    val status: String?,
    val date: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(n: CoachNote) = CoachNoteResponse(
            id = n.id,
            memberId = n.memberId,
            text = n.text,
            status = n.status,
            date = n.date.toString(),
            createdAt = n.createdAt
        )
    }
}

data class PeerFeedbackResponse(
    val id: String,
    val memberId: String,
    val authorId: String?,
    val authorName: String?,
    val status: String,
    val strengths: String?,
    val improve: String?,
    val date: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(f: PeerFeedback) = PeerFeedbackResponse(
            id = f.id,
            memberId = f.memberId,
            authorId = f.authorId,
            authorName = f.authorName,
            status = f.status,
            strengths = f.strengths,
            improve = f.improve,
            date = f.date.toString(),
            createdAt = f.createdAt
        )
    }
}

data class VODResponse(
    val id: String,
    val memberId: String,
    val title: String,
    val url: String,
    val date: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(v: VOD) = VODResponse(
            id = v.id,
            memberId = v.memberId,
            title = v.title,
            url = v.url,
            date = v.date.toString(),
            createdAt = v.createdAt
        )
    }
}
