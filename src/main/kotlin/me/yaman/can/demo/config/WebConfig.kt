package me.yaman.can.demo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class WebConfig {
    @Bean
    fun indexRouter(@Value("classpath:/static/index.html") html: Resource) = router {
        GET("/api") {
            ok().contentType(MediaType.TEXT_HTML).bodyValue(html)
        }
    }
}
