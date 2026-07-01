package com.iris.repository

import com.iris.model.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface EventRepository : JpaRepository<Event, String> {
    fun findByTeamId(teamId: String): List<Event>
    fun findByDateBetween(from: LocalDate, to: LocalDate): List<Event>
    fun findByTeamIdAndDateBetween(teamId: String, from: LocalDate, to: LocalDate): List<Event>
}
