package com.iris.service

import com.iris.dto.request.*
import com.iris.dto.response.*
import com.iris.model.entity.*
import com.iris.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class EventService(
    private val eventRepo: EventRepository,
    private val eventParticipantRepo: EventParticipantRepository,
    private val memberRepo: MemberRepository
) {
    fun getAll(from: LocalDate?, to: LocalDate?): List<EventResponse> {
        val events = when {
            from != null && to != null -> eventRepo.findByDateBetween(from, to)
            else -> eventRepo.findAll()
        }
        return events.map { toResponse(it) }
    }

    fun getById(id: String): EventResponse? {
        val opt = eventRepo.findById(id)
        if (!opt.isPresent) return null
        return toResponse(opt.get())
    }

    @Transactional
    fun create(req: CreateEventRequest): EventResponse {
        val event = Event(
            title = req.title,
            type = req.type,
            date = req.date,
            startTime = req.startTime,
            endTime = req.endTime,
            location = req.location,
            mapUrl = req.mapUrl,
            notes = req.notes
        )
        val saved = eventRepo.save(event)

        req.participantIds.orEmpty().distinct().forEach { memberId ->
            eventParticipantRepo.save(EventParticipant(eventId = saved.id, memberId = memberId))
        }

        return toResponse(saved)
    }

    @Transactional
    fun update(id: String, req: UpdateEventRequest): EventResponse? {
        val opt = eventRepo.findById(id)
        if (!opt.isPresent) return null
        val event = opt.get()
        req.title?.let { event.title = it }
        req.type?.let { event.type = it }
        req.date?.let { event.date = it }
        req.startTime?.let { event.startTime = it }
        req.endTime?.let { event.endTime = it }
        req.location?.let { event.location = it }
        req.mapUrl?.let { event.mapUrl = it }
        req.notes?.let { event.notes = it }
        val saved = eventRepo.save(event)

        // Update participants if provided
        req.participantIds?.let { newParticipantIds ->
            // Remove existing participants
            val existing = eventParticipantRepo.findByEventId(id)
            existing.forEach { ep ->
                eventParticipantRepo.delete(ep)
            }
            // Add new participants
            newParticipantIds.distinct().forEach { memberId ->
                eventParticipantRepo.save(EventParticipant(eventId = id, memberId = memberId))
            }
        }

        return toResponse(saved)
    }

    fun delete(id: String): Boolean {
        if (!eventRepo.existsById(id)) return false
        eventRepo.deleteById(id)
        return true
    }

    fun addParticipant(eventId: String, memberId: String): Boolean {
        if (!eventRepo.existsById(eventId) || !memberRepo.existsById(memberId)) return false
        val existing = eventParticipantRepo.findByEventId(eventId)
        if (existing.any { it.memberId == memberId }) return true
        eventParticipantRepo.save(EventParticipant(eventId = eventId, memberId = memberId))
        return true
    }

    fun removeParticipant(eventId: String, memberId: String): Boolean {
        if (!eventRepo.existsById(eventId)) return false
        eventParticipantRepo.deleteById(EventParticipantId(eventId, memberId))
        return true
    }

    private fun toResponse(event: Event): EventResponse {
        val participants = eventParticipantRepo.findByEventId(event.id).map { it.memberId }
        return EventResponse.from(event, participants)
    }
}
