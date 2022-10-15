package io.github.grishaninvyacheslav.polus_dispatcher.domain.entities

data class ExecutorEntity(
    val executorId: String,
    val name: String,
    val login: String,
    val lon: String,
    val lat: String,
    val status: String,
    val roles: List<String>,
    val jobId: String
)