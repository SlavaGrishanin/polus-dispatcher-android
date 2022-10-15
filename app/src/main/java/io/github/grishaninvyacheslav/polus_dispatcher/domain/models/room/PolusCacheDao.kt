package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PolusCacheDao {
    @Insert
    suspend fun insert(vararg users: PolusCacheListing)

    @Query("SELECT * FROM PolusCacheListing WHERE executorId LIKE :cashedPage LIMIT 1")
    suspend fun getByPage(cashedPage: Int): PolusCacheListing?
}