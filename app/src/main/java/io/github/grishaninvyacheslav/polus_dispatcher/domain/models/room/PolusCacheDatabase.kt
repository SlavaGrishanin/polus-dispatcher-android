package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [PolusCacheListing::class], version = 1)
@TypeConverters(Converters::class)
abstract class PolusCacheDatabase : RoomDatabase() {
    abstract fun dao(): PolusCacheDao
}