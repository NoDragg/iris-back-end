package com.iris.repository

import com.iris.model.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, String> {
    fun findByTeamId(teamId: String): List<Member>
}
