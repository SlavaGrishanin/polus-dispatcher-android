package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity

class Converters {
    @TypeConverter
    fun listToJson(value: List<JobExpandedEntity>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<JobExpandedEntity>::class.java).toList()
}