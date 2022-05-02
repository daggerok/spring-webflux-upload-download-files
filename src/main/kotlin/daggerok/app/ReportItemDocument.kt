package daggerok.app

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant

data class ReportItemDocument(
    @JsonIgnore val id: Long? = null,
    @JsonIgnore val jobId: Long = -1,
    val name: String = "",
    val content: Number = -1,
    val lastModifiedAt: Instant = Instant.now(),
)
