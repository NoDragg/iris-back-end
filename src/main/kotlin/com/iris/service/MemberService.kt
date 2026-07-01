package com.iris.service

import com.iris.dto.request.*
import com.iris.dto.response.*
import com.iris.model.entity.*
import com.iris.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MemberService(
    private val memberRepo: MemberRepository,
    private val leaderNoteRepo: LeaderNoteRepository,
    private val coachNoteRepo: CoachNoteRepository,
    private val peerFeedbackRepo: PeerFeedbackRepository,
    private val vodRepo: VODRepository
) {
    fun getAll(): List<MemberResponse> = memberRepo.findAll().map { MemberResponse.from(it) }

    fun getById(id: String): MemberResponse? = memberRepo.findById(id).let { if (it.isPresent) MemberResponse.from(it.get()) else null }

    fun create(req: CreateMemberRequest): MemberResponse {
        val member = Member(
            name = req.name,
            role = req.role,
            age = req.age,
            location = req.location,
            rank = req.rank,
            position = req.position,
            aim = req.aim ?: 50,
            gameSense = req.gameSense ?: 50,
            teamwork = req.teamwork ?: 50,
            communication = req.communication ?: 50
        )
        return MemberResponse.from(memberRepo.save(member))
    }

    fun update(id: String, req: UpdateMemberRequest): MemberResponse? {
        val opt = memberRepo.findById(id)
        if (!opt.isPresent) return null
        val member = opt.get()
        req.name?.let { member.name = it }
        req.role?.let { member.role = it }
        req.age?.let { member.age = it }
        req.location?.let { member.location = it }
        req.rank?.let { member.rank = it }
        req.position?.let { member.position = it }
        req.avatarUrl?.let { member.avatarUrl = it }
        return MemberResponse.from(memberRepo.save(member))
    }

    fun updateStats(id: String, req: UpdateStatsRequest): MemberResponse? {
        val opt = memberRepo.findById(id)
        if (!opt.isPresent) return null
        val member = opt.get()
        req.aim?.let { member.aim = it }
        req.gameSense?.let { member.gameSense = it }
        req.teamwork?.let { member.teamwork = it }
        req.communication?.let { member.communication = it }
        return MemberResponse.from(memberRepo.save(member))
    }

    fun delete(id: String): Boolean {
        if (!memberRepo.existsById(id)) return false
        memberRepo.deleteById(id)
        return true
    }

    // Leader Notes
    fun getLeaderNotes(memberId: String) = leaderNoteRepo.findByMemberIdOrderByDateDesc(memberId).map { LeaderNoteResponse.from(it) }

    fun addLeaderNote(memberId: String, req: AddNoteRequest): LeaderNoteResponse {
        val note = LeaderNote(memberId = memberId, text = req.text, date = req.date ?: LocalDate.now())
        return LeaderNoteResponse.from(leaderNoteRepo.save(note))
    }

    fun deleteLeaderNote(noteId: String): Boolean {
        if (!leaderNoteRepo.existsById(noteId)) return false
        leaderNoteRepo.deleteById(noteId)
        return true
    }

    // Coach Notes
    fun getCoachNotes(memberId: String) = coachNoteRepo.findByMemberIdOrderByDateDesc(memberId).map { CoachNoteResponse.from(it) }

    fun addCoachNote(memberId: String, req: AddCoachNoteRequest): CoachNoteResponse {
        val note = CoachNote(memberId = memberId, text = req.text, status = req.status, date = req.date ?: LocalDate.now())
        return CoachNoteResponse.from(coachNoteRepo.save(note))
    }

    fun deleteCoachNote(noteId: String): Boolean {
        if (!coachNoteRepo.existsById(noteId)) return false
        coachNoteRepo.deleteById(noteId)
        return true
    }

    // Peer Feedback
    fun getPeerFeedback(memberId: String) = peerFeedbackRepo.findByMemberIdOrderByDateDesc(memberId).map { PeerFeedbackResponse.from(it) }

    fun addPeerFeedback(memberId: String, req: AddPeerFeedbackRequest): PeerFeedbackResponse {
        val feedback = PeerFeedback(
            memberId = memberId,
            status = req.status,
            strengths = req.strengths,
            improve = req.improve,
            authorId = req.authorId,
            authorName = req.authorName,
            date = req.date ?: LocalDate.now()
        )
        return PeerFeedbackResponse.from(peerFeedbackRepo.save(feedback))
    }

    fun deletePeerFeedback(feedbackId: String): Boolean {
        if (!peerFeedbackRepo.existsById(feedbackId)) return false
        peerFeedbackRepo.deleteById(feedbackId)
        return true
    }

    // VODs
    fun getVODs(memberId: String) = vodRepo.findByMemberIdOrderByDateDesc(memberId).map { VODResponse.from(it) }

    fun addVOD(memberId: String, req: AddVODRequest): VODResponse {
        val vod = VOD(memberId = memberId, title = req.title, url = req.url, date = req.date ?: LocalDate.now())
        return VODResponse.from(vodRepo.save(vod))
    }

    fun deleteVOD(vodId: String): Boolean {
        if (!vodRepo.existsById(vodId)) return false
        vodRepo.deleteById(vodId)
        return true
    }
}
