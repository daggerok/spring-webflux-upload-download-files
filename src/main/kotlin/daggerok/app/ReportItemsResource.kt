package daggerok.app

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isRegularFile
import org.apache.logging.log4j.kotlin.logger
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ZeroCopyHttpOutputMessage
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ReportItemsResource(private val appProps: AppProps) {

    @PostMapping("/api/v1/download/{filename}")
    fun downloadFile(@PathVariable("filename") filename: String, response: ServerHttpResponse): Mono<Void> {
        val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
        zeroCopyResponse.headers[HttpHeaders.CONTENT_DISPOSITION] = "attachment; filename=$filename"
        zeroCopyResponse.headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_OCTET_STREAM_VALUE
        // response.getHeaders().let {
        //     it[HttpHeaders.CONTENT_DISPOSITION] = "attachment; filename=$name"
        //     it.contentType = MediaType.APPLICATION_OCTET_STREAM
        // }
        val filepath = appProps.toBasePath().resolve(filename).absolutePathString()
        // val file = ClassPathResource(filepath).file
        val file = File(filepath)
        return zeroCopyResponse.writeWith(file, 0, file.length())
    }

    @PostMapping("/api/v1/upload")
    fun uploadFileV1(@RequestPart("file") file: FilePart): Mono<ReportItemDocument> =
        Mono
            .fromCallable {
                val path = appProps.toBasePath().resolve(file.filename()).apply { deleteIfExists() }
                file.transferTo(path).subscribe()
            }
            .flatMap { Mono.from(file.content()) }
            .map {
                ByteArray(it.readableByteCount()).apply {
                    it.read(this)
                    DataBufferUtils.release(it)
                }
            }
            .map { appProps.toBasePath().resolve(file.filename()).toReportItemDocument(it.size) }
            .doOnNext { log.info { "uploadFileV1: $it" } }

    @GetMapping("/api/v1/uploads")
    fun listUploadedFiles(): Flux<ReportItemDocument> =
        Flux
            .fromStream(
                Files.walk(appProps.toBasePath(), Int.MAX_VALUE)
                    .filter { it.isRegularFile() }
            )
            .map { it.toReportItemDocument() }
            .doOnNext { log.info { "listUploadedFiles: $it" } }

    private fun Path.toReportItemDocument(fileSize: Number = -1): ReportItemDocument =
        toFile().let {
            ReportItemDocument(
                name = it.name,
                content = if (fileSize != -1) fileSize else it.length(),
                lastModifiedAt = Instant.ofEpochMilli(it.lastModified()),
            )
        }

    private companion object { val log = logger() }
}
