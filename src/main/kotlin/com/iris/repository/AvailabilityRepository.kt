package com.iris.repository

import com.iris.model.entity.Availability
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AvailabilityRepository : JpaRepository<Availability, String> {
    fun findByMemberIdAndWeekKey(memberId: String, weekKey: LocalDate): List<Availability>
    fun findByWeekKey(weekKey: LocalDate): List<Availability>
    fun deleteByMemberIdAndWeekKeyAndDayIndex(memberId: String, weekKey: LocalDate, dayIndex: Int)
}
