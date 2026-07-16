package com.iris.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.iris.model.entity.Event
import com.iris.model.entity.EventParticipant
import com.iris.model.entity.Member
import com.iris.repository.EventParticipantRepository
import com.iris.repository.EventRepository
import com.iris.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest(
    properties = [
        "spring.datasource.url=jdbc:h2:mem:event-test;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
    ]
)
@AutoConfigureMockMvc
class EventPersistenceIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val eventRepo: EventRepository,
    private val eventParticipantRepo: EventParticipantRepository,
    private val memberRepo: MemberRepository
) {
    @BeforeEach
    fun setUp() {
        eventParticipantRepo.deleteAll()
        eventRepo.deleteAll()
        memberRepo.deleteAll()
        memberRepo.saveAll(
            listOf(
                Member(id = "member-1", name = "One"),
                Member(id = "member-2", name = "Two")
            )
        )
    }

    @Test
    fun `create persists every field sent by the frontend`() {
        val response = mockMvc.post("/api/v1/events") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "title": "Practice",
                  "type": "Scrim",
                  "date": "2026-07-20",
                  "startTime": "09:15",
                  "endTime": "12:30",
                  "location": "Arena",
                  "map": "https://maps.example/create",
                  "notes": "Bring gear",
                  "participants": ["member-1", "member-2"]
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn().response

        val id = objectMapper.readTree(response.contentAsString)["id"].asText()
        val saved = eventRepo.findById(id).orElseThrow()

        assertThat(saved.title).isEqualTo("Practice")
        assertThat(saved.type).isEqualTo("Scrim")
        assertThat(saved.date).isEqualTo(LocalDate.parse("2026-07-20"))
        assertThat(saved.startTime).isEqualTo(LocalTime.parse("09:15"))
        assertThat(saved.endTime).isEqualTo(LocalTime.parse("12:30"))
        assertThat(saved.location).isEqualTo("Arena")
        assertThat(saved.mapUrl).isEqualTo("https://maps.example/create")
        assertThat(saved.notes).isEqualTo("Bring gear")
        assertThat(eventParticipantRepo.findByEventId(id).map { it.memberId })
            .containsExactlyInAnyOrder("member-1", "member-2")

        val body = objectMapper.readTree(response.contentAsString)
        assertThat(body["map"].asText()).isEqualTo("https://maps.example/create")
        assertThat(body["participants"].map { it.asText() })
            .containsExactly("member-1", "member-2")
    }

    @Test
    fun `update persists every field sent by the frontend`() {
        val event = eventRepo.save(
            Event(
                id = "event-1",
                title = "Old",
                type = "Practice",
                date = LocalDate.parse("2026-07-20"),
                startTime = LocalTime.parse("09:00"),
                endTime = LocalTime.parse("10:00"),
                location = "Old place",
                mapUrl = "https://maps.example/old",
                notes = "Old notes"
            )
        )
        eventParticipantRepo.save(EventParticipant(event.id, "member-1"))

        val response = mockMvc.put("/api/v1/events/${event.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "title": "Updated",
                  "type": "Tournament",
                  "date": "2026-07-21",
                  "startTime": "13:15",
                  "endTime": "16:45",
                  "location": "New arena",
                  "map": "https://maps.example/update",
                  "notes": "Updated notes",
                  "participants": ["member-2"]
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
        }.andReturn().response

        val saved = eventRepo.findById(event.id).orElseThrow()
        assertThat(saved.title).isEqualTo("Updated")
        assertThat(saved.type).isEqualTo("Tournament")
        assertThat(saved.date).isEqualTo(LocalDate.parse("2026-07-21"))
        assertThat(saved.startTime).isEqualTo(LocalTime.parse("13:15"))
        assertThat(saved.endTime).isEqualTo(LocalTime.parse("16:45"))
        assertThat(saved.location).isEqualTo("New arena")
        assertThat(saved.mapUrl).isEqualTo("https://maps.example/update")
        assertThat(saved.notes).isEqualTo("Updated notes")
        assertThat(eventParticipantRepo.findByEventId(event.id).map { it.memberId })
            .containsExactly("member-2")

        val body = objectMapper.readTree(response.contentAsString)
        assertThat(body["map"].asText()).isEqualTo("https://maps.example/update")
        assertThat(body["participants"].map { it.asText() }).containsExactly("member-2")
    }
}
