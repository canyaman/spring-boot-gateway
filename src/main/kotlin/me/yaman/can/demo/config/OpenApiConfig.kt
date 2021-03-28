package me.yaman.can.demo.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import org.springdoc.core.ActuatorProvider
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.SpringDocConfigProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.cloud.gateway.route.RouteDefinition
import org.springframework.cloud.gateway.route.RouteDefinitionLocator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping
import java.util.Optional

@Configuration
@OpenAPIDefinition()
class OpenApiConfig(val locator: RouteDefinitionLocator) {

    @Bean
    fun swaggerConfigure(
        @Value("\${spring.application.name}") applicationName: String,
        buildProperties: BuildProperties,
        gitProperties: GitProperties,
        asciidocDocumentation: ExternalDocumentation?
    ): OpenAPI {
        val openApi = OpenAPI()
            .components(Components())
            .info(
                io.swagger.v3.oas.models.info.Info()
                    .title(applicationName)
                    .version(buildProperties.version)
                    .description("**${buildProperties.group}:${buildProperties.name}** GIT ${gitProperties.branch}:${gitProperties.shortCommitId} @ ${gitProperties.commitTime}")
            ).externalDocs(asciidocDocumentation)
        return openApi
    }

    @Bean
    fun groupedOpenApis(
        requestMappingHandlerMapping: RequestMappingInfoHandlerMapping?,
        springDocConfigProperties: SpringDocConfigProperties,
        actuatorProvider: Optional<ActuatorProvider?>?
    ): List<GroupedOpenApi> {
        val definitions = locator.routeDefinitions.collectList().block()!!
        val groups = definitions.filter { routeDefinition: RouteDefinition ->
            routeDefinition.id.endsWith("-app")
        }.map { routeDefinition: RouteDefinition ->
            val name = routeDefinition.id.replace("-app$".toRegex(), "")
            val path: String = routeDefinition.metadata["context-path"] as? String
                ?: routeDefinition.metadata["contextPath"] as? String
                ?: routeDefinition.metadata["contextpath"] as? String
                ?: routeDefinition.metadata["CONTEXTPATH"] as? String
                ?: name
            GroupedOpenApi.builder().pathsToMatch("/$path/**").group(path).build()
        }
        groups.forEach { item ->
            val groupConfig = SpringDocConfigProperties.GroupConfig(
                item.group,
                item.pathsToMatch,
                item.packagesToScan,
                item.packagesToExclude,
                item.pathsToExclude,
                item.producesToMatch,
                item.consumesToMatch,
                item.headersToMatch
            )
            springDocConfigProperties.addGroupConfig(groupConfig)
        }
        return groups
    }
}
