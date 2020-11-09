package com.lcz.bm.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * desc: TODO
 * date: 2019/9/19 11:34 by lcz
 */

interface PreferenceStorage {
    var phone: String?
    var password: String?

    /*YQW*/
    var id: Int?
    var token: String?
}

class SharedPreferenceStorage @Inject constructor(private val context: Context) :
    PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(changeListener)
        }
    }

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
    }

    override var phone: String? by StringPreference(
        prefs, PREF_PHONE, ""
    )
    override var password: String? by StringPreference(
        prefs, PREF_PASS_WORD, ""
    )

    override var id: Int? by IntPreference(
        prefs, PREF_QIW_ID, -1
    )


    override var token by StringPreference(
        prefs, PREF_QIW_TOKEN, ""
    )

    companion object {


        const val PREFS_NAME = "lczBM"
        const val PREF_PASS_WORD = "pref_pass_word"
        const val PREF_PHONE = "pref_phone"

        /*YQW*/
        const val PREF_QIW_ID = "pref_qiw_id"
        const val PREF_QIW_TOKEN = "pref_qiw_token"

    }

}


class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}


class IntPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Int?
) : ReadWriteProperty<Any, Int?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
        return defaultValue?.let { preferences.value.getInt(name, it) }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        preferences.value.edit { value?.let { putInt(name, it) } }
    }
}
