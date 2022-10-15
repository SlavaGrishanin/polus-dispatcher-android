package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity

@Entity
data class PolusCacheListing (
    @PrimaryKey val executorId: String,
    @ColumnInfo(name = "jobs") val jobs: List<JobEntity>
)