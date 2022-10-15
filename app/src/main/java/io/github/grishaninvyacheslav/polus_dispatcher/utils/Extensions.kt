package io.github.grishaninvyacheslav.polus_dispatcher.utils

import android.text.format.DateFormat
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import java.util.*

fun List<JobEntity>.getNearest(currentTimestamp: Long): JobEntity? {
    var nearest: JobEntity? = null
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

fun String.timestampToClockTime(): String {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this.toLong() * 1000
    return DateFormat.format("HH:mm", cal).toString()
}