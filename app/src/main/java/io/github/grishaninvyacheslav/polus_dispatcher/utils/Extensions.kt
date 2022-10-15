package io.github.grishaninvyacheslav.polus_dispatcher.utils

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity

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