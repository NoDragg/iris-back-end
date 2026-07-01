package com.iris.controller

import com.iris.dto.request.*
import com.iris.dto.response.*
import com.iris.service.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
class AvailabilityController(private val availabilityService: AvailabilityService) {

    @GetMapping("/availability")
    fun getAllForWeek(@RequestParam week: LocalDate) =
        ResponseEntity.ok(availabilityService.getAllForWeek(week))

    @GetMapping("/members/{id}/availability")
    fun getByMember(
        @PathVariable id: String,
        @RequestParam week: LocalDate
    ) = availabilityService.getByMember(id, week)

    @PutMapping("/members/{id}/availability")
    fun setAvailability(
        @PathVariable id: String,
        @RequestBody req: SetAvailabilityRequest
    ) = ResponseEntity.ok(availabilityService.setAvailability(id, req))

    @DeleteMapping("/members/{id}/availability/{dayIndex}")
    fun deleteSlot(
        @PathVariable id: String,
        @PathVariable dayIndex: Int,
        @RequestParam week: LocalDate
    ) {
        availabilityService.deleteSlot(id, dayIndex, week)
        ResponseEntity.noContent().build<Unit>()
    }
}
