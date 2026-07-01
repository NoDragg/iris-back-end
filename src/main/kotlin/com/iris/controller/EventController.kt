package com.iris.controller

import com.iris.dto.request.*
import com.iris.dto.response.*
import com.iris.service.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/events")
class EventController(private val eventService: EventService) {

    @GetMapping
    fun getAll(
        @RequestParam from: LocalDate?,
        @RequestParam to: LocalDate?
    ) = eventService.getAll(from, to)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String) = eventService.getById(id)
        ?: ResponseEntity.notFound().build<Unit>()

    @PostMapping
    fun create(@Valid @RequestBody req: CreateEventRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(req))

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody req: UpdateEventRequest) =
        eventService.update(id, req)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build<Unit>()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) =
        if (eventService.delete(id)) ResponseEntity.noContent().build<Unit>()
        else ResponseEntity.notFound().build<Unit>()

    @PostMapping("/{id}/participants")
    fun addParticipant(@PathVariable id: String, @RequestBody req: AddParticipantRequest) =
        if (eventService.addParticipant(id, req.memberId)) ResponseEntity.ok(mapOf("success" to true))
        else ResponseEntity.notFound().build<Unit>()

    @DeleteMapping("/{id}/participants/{memberId}")
    fun removeParticipant(@PathVariable id: String, @PathVariable memberId: String) =
        if (eventService.removeParticipant(id, memberId)) ResponseEntity.noContent().build<Unit>()
        else ResponseEntity.notFound().build<Unit>()
}
