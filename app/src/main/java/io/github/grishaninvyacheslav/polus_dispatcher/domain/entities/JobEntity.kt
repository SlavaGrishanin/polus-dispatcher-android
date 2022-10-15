package io.github.grishaninvyacheslav.polus_dispatcher.domain.entities

data class JobEntity(
    val jobId: String,
    val startDate: String,
    val endDate: String,
    val status: String,
    val lon: String,
    val lat: String,
    val customer: Customer?,
    val requiredVehicle: RequiredVehicle,
    val executorId: String,
    val title: String
)

data class RequiredVehicle(
    val characteristic: String?,
    val model: String?
)

data class Customer(
    val customerId: String,
    val login: String,
    val name: String
)