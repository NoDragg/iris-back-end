package com.iris.repository

import com.iris.model.entity.VOD
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VODRepository : JpaRepository<VOD, String> {
    fun findByMemberIdOrderByDateDesc(memberId: String): List<VOD>
}
