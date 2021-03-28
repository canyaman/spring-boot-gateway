package me.yaman.can.demo

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import me.yaman.can.demo.model.CurrentTime
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ApplicationTests(
    @Autowired val webClient: WebTestClient
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun contextLoads() {
    }

    @Test
    fun timeControllerNowTest() {
        val response = webClient.get().uri("/time/now")
            .exchange()
            .expectStatus().isOk.expectBody(CurrentTime::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.now).isBefore(Instant.now())
    }

    @Test
    fun exceptionTest() {
        val response = webClient.get().uri("/resource/not-exist")
            .exchange()
            .expectStatus().isNotFound.expectBody(JsonNode::class.java).returnResult()
        val service = response.responseBody?.get("service") as? TextNode
        Assertions.assertThat(service?.textValue()).isNotEmpty
    }
}
