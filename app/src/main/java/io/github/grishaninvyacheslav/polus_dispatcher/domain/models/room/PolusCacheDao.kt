package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PolusCacheDao {
    @Insert
    suspend fun insert(vararg users: PolusCacheListing)

    @Query("SELECT * FROM PolusCacheListing WHERE executorId LIKE :id LIMIT 1")
    suspend fun getById(id: Int): PolusCacheListing?

    @Update
    fun updateCache(vararg users: PolusCacheListing)
}