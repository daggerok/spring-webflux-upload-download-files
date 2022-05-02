package daggerok.app

import java.nio.file.Path
import java.nio.file.Paths
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app")
data class AppProps(val baseDir: String = "") {
    fun toBasePath(): Path {
        println("Base dir configuration is missing or incorrect")
        if (baseDir.isBlank()) throw RuntimeException("Base dir configuration is missing or incorrect")
        return Paths.get(baseDir).toAbsolutePath().apply { toFile().mkdirs() }
    }
}
