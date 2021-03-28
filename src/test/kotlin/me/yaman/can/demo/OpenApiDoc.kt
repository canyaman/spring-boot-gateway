package me.yaman.can.demo

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.yaml.snakeyaml.Yaml
import java.io.FileWriter
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.webflux.basePath=/"
    ]
)
@ActiveProfiles("open-api")
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST")
@EnableAutoConfiguration()
@Tag("open-api")
class OpenApiDoc(
    @LocalServerPort val port: Integer,
    @Value("\${spring.application.name}") val appName: String
) {
    private val yamlFilePath = "src/main/resources/api-docs/$appName.yml"
    private val openApiDocsUrl = "http://localhost:$port/v3/api-docs"

    @Test
    fun generateApiDocsYaml() {
        val remoteYamlUrl = URL(openApiDocsUrl)
        val resourceFile = Paths.get(yamlFilePath)
        if (!resourceFile.toFile().exists()) {
            resourceFile.parent.toFile().mkdirs()
            resourceFile.toFile().createNewFile()
        }
        remoteYamlUrl.openStream().use { `in` -> Files.copy(`in`, resourceFile, StandardCopyOption.REPLACE_EXISTING) }

        val yaml = Yaml()
        lateinit var content: MutableMap<String, Any>
        resourceFile.toFile().inputStream().use { `in` -> content = yaml.load(`in`) }

        val description = "Spring boot application auto generated OpenApi doc."
        (content["info"] as? MutableMap<String, String>)?.set("description", description)
        ((content["servers"] as? ArrayList<*>)?.first() as? MutableMap<String, String>)?.set("url", "http://$appName:80")
        FileWriter(resourceFile.toString()).use { yaml.dump(content, it) }
    }
}
