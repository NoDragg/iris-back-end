package com.iris.repository

import com.iris.model.entity.EventParticipant
import com.iris.model.entity.EventParticipantId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventParticipantRepository : JpaRepository<EventParticipant, EventParticipantId> {
    fun findByEventId(eventId: String): List<EventParticipant>
    fun findByMemberId(memberId: String): List<EventParticipant>
    fun deleteByEventIdAndMemberId(eventId: String, memberId: String)
}
