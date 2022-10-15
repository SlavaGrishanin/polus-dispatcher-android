package io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.preferences

interface IPreferencesRepository {
    fun saveInt(key: String, value: Int)
    fun getInt(key: String): Int
    fun removeInt(key: String)
}