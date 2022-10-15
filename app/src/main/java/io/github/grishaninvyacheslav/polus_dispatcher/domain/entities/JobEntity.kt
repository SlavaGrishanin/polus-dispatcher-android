package io.github.grishaninvyacheslav.polus_dispatcher.domain.entities

data class JobEntity(
    val jobId: String,
    val startDate: String,
    val endDate: String,
    val status: String,
    val lon: String,
    val lat: String,
    val customerId: String,
    val requiredVehicle: RequiredVehicle,
    val executorId: String,
    val title: String
)

data class RequiredVehicle(
    val characteristic: String?,
    val model: String?
)