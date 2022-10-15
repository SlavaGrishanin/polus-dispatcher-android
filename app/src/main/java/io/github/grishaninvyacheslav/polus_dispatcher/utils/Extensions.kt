package io.github.grishaninvyacheslav.polus_dispatcher.utils

import android.text.format.DateFormat
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.RequiredVehicle
import java.util.*

fun List<JobExpandedEntity>.getNearest(currentTimestamp: Long): JobExpandedEntity? {
    var nearest: JobExpandedEntity? = null
    for (job in this) {
        if (
            (
                    nearest == null ||
                            job.startDate.toLong() < nearest.startDate.toLong()
                    ) &&
            job.startDate.toLong() > currentTimestamp
        ) {
            nearest = job
        }
    }
    return nearest
}

fun Long.timestampToClockTime(): String {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000
    return DateFormat.format("HH:mm", cal).toString()
}

fun Long.timestampToDate(): String {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000
    return DateFormat.format("HH:mm dd.MM.yy", cal).toString()
}

fun JobExpandedEntity.toJobEntity(): JobEntity {
    return JobEntity(
        customerId = this.customer.id,
        endDate = this.endDate,
        executorId = this.executor.id,
        id = this.id,
        lat = this.lat,
        lon = this.lon,
        requiredVehicle = RequiredVehicle(
            this.typeVehicle,
            this.modelVehicle
        ),
        startDate = this.startDate,
        status = this.status,
        title = this.title
    )
}