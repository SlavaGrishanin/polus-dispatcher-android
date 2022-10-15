package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity

@Entity
data class PolusCacheListing (
    @PrimaryKey val executorId: Int,
    @ColumnInfo(name = "jobs") val jobs: List<JobExpandedEntity>
)