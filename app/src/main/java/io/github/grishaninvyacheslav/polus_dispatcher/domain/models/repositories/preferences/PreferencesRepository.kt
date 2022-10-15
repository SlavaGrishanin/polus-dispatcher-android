package io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.preferences

import android.content.Context
import io.github.grishaninvyacheslav.polus_dispatcher.BuildConfig

class PreferencesRepository(
    context: Context
) : IPreferencesRepository {
    override fun saveInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    override fun getInt(key: String): Int {
        return sharedPref.getInt(key, -1)
    }

    override fun removeInt(key: String) {
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