package com.iris.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class SetAvailabilityRequest(
    val week: LocalDate,
    val slots: List<AvailabilitySlot>
)

data class AvailabilitySlot(
    val dayIndex: Int,
    val status: String = "available",
    val startHour: Double? = null,
    val endHour: Double? = null
)
