package com.iris.repository

import com.iris.model.entity.LeaderNote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaderNoteRepository : JpaRepository<LeaderNote, String> {
    fun findByMemberIdOrderByDateDesc(memberId: String): List<LeaderNote>
}
