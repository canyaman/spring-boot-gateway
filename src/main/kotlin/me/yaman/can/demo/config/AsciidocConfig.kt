package me.yaman.can.demo.config

import io.swagger.v3.oas.models.ExternalDocumentation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
@Profile("asciidoc")
class AsciidocConfig {
    val path = "/asciidoc/**"

    @Bean
    fun asciidocPages(): RouterFunction<ServerResponse> {
        return RouterFunctions.resources(path, ClassPathResource("asciidoc/"))
    }

    @Bean
    fun asciidocDocumentation(): ExternalDocumentation {
        val asciidoc = ExternalDocumentation()
        asciidoc.description = "Online Documentation"
        asciidoc.url = "/asciidoc/index.html"
        return asciidoc
    }
}
