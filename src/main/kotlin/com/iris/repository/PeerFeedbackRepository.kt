package com.iris.repository

import com.iris.model.entity.PeerFeedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PeerFeedbackRepository : JpaRepository<PeerFeedback, String> {
    fun findByMemberIdOrderByDateDesc(memberId: String): List<PeerFeedback>
}
