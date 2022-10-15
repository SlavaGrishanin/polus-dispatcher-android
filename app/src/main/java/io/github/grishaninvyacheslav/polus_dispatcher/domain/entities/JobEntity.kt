package io.github.grishaninvyacheslav.polus_dispatcher.domain.entities

data class JobEntity(
    val customerId: Int,
    val endDate: Long,
    val executorId: Int,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val requiredVehicle: RequiredVehicle,
    val startDate: Long,
    val status: String,
    val title: String
)

data class RequiredVehicle(
    val type: String,
    val model: String
)