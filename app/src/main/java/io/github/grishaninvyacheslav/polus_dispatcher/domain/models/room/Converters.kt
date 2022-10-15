package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity

class Converters {
    @TypeConverter
    fun listToJson(value: List<JobEntity>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<JobEntity>::class.java).toList()
}