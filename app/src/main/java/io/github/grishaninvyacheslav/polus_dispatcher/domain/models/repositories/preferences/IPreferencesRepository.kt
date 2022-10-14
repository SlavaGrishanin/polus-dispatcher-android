package io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.preferences

interface IPreferencesRepository {
    fun saveString(key: String, value: String)
    fun getString(key: String): String?
    fun removeString(key: String)
}