package com.iris.dto.response

import com.iris.model.entity.Availability
import java.time.LocalDate

data class AvailabilityResponse(
    val weekKey: LocalDate,
    val members: List<MemberAvailability>
)

data class MemberAvailability(
    val memberId: String,
    val memberName: String,
    val slots: List<SlotResponse>
)

data class SlotResponse(
    val dayIndex: Int,
    val start: Double,
    val end: Double
) {
    companion object {
        fun from(a: Availability) = SlotResponse(a.dayIndex, a.startHour, a.endHour)
    }
}
