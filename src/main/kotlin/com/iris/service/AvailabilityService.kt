package com.iris.service

import com.iris.dto.request.*
import com.iris.dto.response.*
import com.iris.model.entity.Availability
import com.iris.repository.*
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class AvailabilityService(
    private val availabilityRepo: AvailabilityRepository,
    private val memberRepo: MemberRepository
) {
    fun getByMember(memberId: String, weekKey: LocalDate): List<SlotResponse> {
        return availabilityRepo.findByMemberIdAndWeekKey(memberId, weekKey)
            .map { SlotResponse.from(it) }
    }

    fun getAllForWeek(weekKey: LocalDate): AvailabilityResponse {
        val availabilities = availabilityRepo.findByWeekKey(weekKey)
        val memberIds = availabilities.map { it.memberId }.distinct()

        val members = memberIds.mapNotNull { id ->
            val opt = memberRepo.findById(id)
            if (opt.isPresent) {
                val m = opt.get()
                val slots = availabilities.filter { it.memberId == id }.map { SlotResponse.from(it) }
                MemberAvailability(m.id, m.name, slots)
            } else null
        }

        return AvailabilityResponse(weekKey, members)
    }

    fun setAvailability(memberId: String, req: SetAvailabilityRequest): List<SlotResponse> {
        // Delete existing slots for this member/week
        availabilityRepo.findByMemberIdAndWeekKey(memberId, req.week).forEach {
            availabilityRepo.delete(it)
        }

        // Insert new slots
        val saved = req.slots.map { slot ->
            availabilityRepo.save(Availability(
                memberId = memberId,
                weekKey = req.week,
                dayIndex = slot.dayIndex,
                startHour = slot.startHour ?: 0,
                endHour = slot.endHour ?: 24
            ))
        }

        return saved.map { SlotResponse.from(it) }
    }

    fun deleteSlot(memberId: String, dayIndex: Int, weekKey: LocalDate): Boolean {
        availabilityRepo.deleteByMemberIdAndWeekKeyAndDayIndex(memberId, weekKey, dayIndex)
        return true
    }

    companion object {
        fun getWeekKey(date: LocalDate): LocalDate {
            return date.with(DayOfWeek.MONDAY)
        }
    }
}
