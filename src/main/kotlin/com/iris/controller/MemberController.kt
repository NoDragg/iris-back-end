package com.iris.controller

import com.iris.dto.request.*
import com.iris.dto.response.*
import com.iris.service.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/members")
class MemberController(private val memberService: MemberService, private val cloudinaryService: CloudinaryService) {

    @GetMapping
    fun getAll() = memberService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String) = memberService.getById(id)
        ?: ResponseEntity.notFound().build<Unit>()

    @PostMapping
    fun create(@Valid @RequestBody req: CreateMemberRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(req))

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody req: UpdateMemberRequest) =
        memberService.update(id, req)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build<Unit>()

    @PutMapping("/{id}/stats")
    fun updateStats(@PathVariable id: String, @RequestBody req: UpdateStatsRequest) =
        memberService.updateStats(id, req)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build<Unit>()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) =
        if (memberService.delete(id)) ResponseEntity.noContent().build<Unit>()
        else ResponseEntity.notFound().build<Unit>()

    @PostMapping("/{id}/avatar")
    fun uploadAvatar(@PathVariable id: String, @RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, String>> {
        val avatarUrl = cloudinaryService.uploadAvatar(file)
        memberService.update(id, UpdateMemberRequest(avatarUrl = avatarUrl))
        return ResponseEntity.ok(mapOf("avatarUrl" to avatarUrl))
    }

    // Leader Notes
    @GetMapping("/{id}/leader-notes")
    fun getLeaderNotes(@PathVariable id: String) = memberService.getLeaderNotes(id)

    @PostMapping("/{id}/leader-notes")
    fun addLeaderNote(@PathVariable id: String, @Valid @RequestBody req: AddNoteRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(memberService.addLeaderNote(id, req))

    @DeleteMapping("/leader-notes/{noteId}")
    fun deleteLeaderNote(@PathVariable noteId: String) =
        if (memberService.deleteLeaderNote(noteId)) ResponseEntity.noContent().build<Unit>()
        else ResponseEntity.notFound().build<Unit>()

    // Coach Notes
    @GetMapping("/{id}/coach-notes")
    fun getCoachNotes(@PathVariable id: String) = memberService.getCoachNotes(id)

    @PostMapping("/{id}/coach-notes")
    fun addCoachNote(@PathVariable id: String, @Valid @RequestBody req: AddCoachNoteRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(memberService.addCoachNote(id, req))

    @DeleteMapping("/coach-notes/{noteId}")
    fun deleteCoachNote(@PathVariable noteId: String) =
        if (memberService.deleteCoachNote(noteId)) ResponseEntity.noContent().build<Unit>()
        else ResponseEntity.notFound().build<Unit>()

    // Peer Feedback
    @GetMapping("/{id}/peer-feedback")
    fun getPeerFeedback(@PathVariable id: String) = memberService.getPeerFeedback(id)

    @PostMapping("/{id}/peer-feedback")
    fun addPeerFeedback(@PathVariable id: String, @Valid @RequestBody req: AddPeerFeedbackRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(memberService.addPeerFeedback(id, req))

    @DeleteMapping("/peer-feedback/{feedbackId}")
    fun deletePeerFeedback(@PathVariable feedbackId: String) =
        if (memberService.deletePeerFeedback(feedbackId)) ResponseEntity.noContent().build<Unit>()
        else ResponseEntity.notFound().build<Unit>()

    // VODs
    @GetMapping("/{id}/vods")
    fun getVODs(@PathVariable id: String) = memberService.getVODs(id)

    @PostMapping("/{id}/vods")
    fun addVOD(@PathVariable id: String, @Valid @RequestBody req: AddVODRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(memberService.addVOD(id, req))

    @DeleteMapping("/vods/{vodId}")
    fun deleteVOD(@PathVariable vodId: String) =
        if (memberService.deleteVOD(vodId)) ResponseEntity.noContent().build<Unit>()
        else ResponseEntity.notFound().build<Unit>()
}
