package io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.preferences

import android.content.Context
import io.github.grishaninvyacheslav.polus_dispatcher.BuildConfig

class PreferencesRepository(
    context: Context
): IPreferencesRepository {
    override fun saveString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    override fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    override fun removeString(key: String){
        editor.remove(key)
        editor.apply()
    }


    private val sharedPref by lazy {
        context.getSharedPreferences(BuildConfig.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    private val editor by lazy {
        sharedPref.edit()
    }
}