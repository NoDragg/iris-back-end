package com.iris.repository

import com.iris.model.entity.CoachNote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoachNoteRepository : JpaRepository<CoachNote, String> {
    fun findByMemberIdOrderByDateDesc(memberId: String): List<CoachNote>
}
