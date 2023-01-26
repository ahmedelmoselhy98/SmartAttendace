package com.elmoselhy.smartattendace.datalayer.session

import android.content.Context
import android.content.SharedPreferences
import com.elmoselhy.smartattendace.datalayer.models.UserModel
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preference {
        private var SHARED_PREFERENCES_FILE = ""
        private val USER_SESSION = "$SHARED_PREFERENCES_FILE.usersession"
        private val LANGUAGE = ".language"
        private val PUSH_TOKEN = "$SHARED_PREFERENCES_FILE.pushtoken"

        val ARABIC = "عربى"
        val ENGLISH = "English"
        private val DARK_MODE = "dark_mode"
        private val DEVICE_ID = "device_id"
        private val AR = "ar"
        private val EN = "en"

        var sharedPref: SharedPreferences? = null
        private var userSession: UserModel? = null
        var context: Context? = null

        @Inject
        constructor(@ApplicationContext context: Context) {
//        sharedPref
            this.context = context
            SHARED_PREFERENCES_FILE = "${context.packageName}.sharedpreferences"
            sharedPref = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE,
                Context.MODE_PRIVATE
            )

        }

        //Save User SessionModel
        fun setUserSession(fUserSession: UserModel) {
            val editor: SharedPreferences.Editor = sharedPref!!.edit()
            val gson = Gson()
            editor.putString(USER_SESSION, gson.toJson(fUserSession))
            editor.apply()
            userSession = fUserSession
        }

        //Get User SessionModel
        fun getUserSession(): UserModel? {
            return Gson().fromJson(
                sharedPref!!.getString(USER_SESSION, null), UserModel::class.java
            )
        }


        //Save Push Token FCM
        fun savePushNotificationToken(token: String?) {
            val editor =
                context!!.getSharedPreferences(PUSH_TOKEN, Context.MODE_PRIVATE).edit()
            editor.putString(PUSH_TOKEN, token)
            editor.apply()
        }


        //Get push token FCM
        fun getPushNotificationToken(): String? {
            val preferences =
                context!!.getSharedPreferences(PUSH_TOKEN, Context.MODE_PRIVATE)
            return preferences.getString(PUSH_TOKEN, "")
        }


        //Save Dark Mode
        fun saveDarkMode(mode: Int) {
            val editor =
                context!!.getSharedPreferences(DARK_MODE, Context.MODE_PRIVATE).edit()
            editor.putInt(DARK_MODE, mode)
            editor.apply()
        }

        //Get Dark Mode
        fun getDarkMode(): Int {
            val preferences =
                context!!.getSharedPreferences(DARK_MODE, Context.MODE_PRIVATE)
            return preferences.getInt(DARK_MODE, -1)
        }

        //Save device id
        fun saveDeviceId(deviceId: String?) {
            val editor =
                context!!.getSharedPreferences(DEVICE_ID, Context.MODE_PRIVATE).edit()
            editor.putString(DEVICE_ID, deviceId)
            editor.apply()
        }

        //Get device id
        fun getDeviceId(): String? {
            val preferences =
                context!!.getSharedPreferences(DEVICE_ID, Context.MODE_PRIVATE)
            return preferences.getString(DEVICE_ID, "")
        }

        //Get App Language
        fun getAppLanguage(): String? {
            val code: String = getLanguagesCodes().get(getLanguageIndex(context))
            return if (code == AR) "ar-EG" else "en-US"
        }

        //Logout
        fun logout(sessionCallBack: SessionCallBack) {
            // Clear saved data
            val editor = sharedPref!!.edit()
            editor.remove(USER_SESSION)
            editor.apply()
            // Reset session
            userSession = null
            // Notify
            sessionCallBack.setOnLogout()
        }

        //clear session
        fun clearUserSession(context: Context?) {
            // Clear saved data
            val editor = sharedPref!!.edit()
            editor.remove(USER_SESSION)
            editor.apply()
            // Reset session
            userSession = null
        }

        //Set User Lang
        fun setUserLanguageSession(Language: String, onSessionUpdate: () -> Unit) {
            if (getUserLanguage(context) != Language) {
                val editor =
                    context!!.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE).edit()
                editor.putString(LANGUAGE, Language)
                editor.apply()
                setLocale(getUserLanguageCode(), context!!)
                onSessionUpdate()
            }
        }

        //Get User language
        fun getUserLanguage(context: Context?): String {
            val lang = this.context!!.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
                .getString(LANGUAGE, null)
            return lang ?: ARABIC
        }

        //Check if lang IsSelected
        fun isLangSelected(context: Context?): Boolean {
            val lang = this.context!!.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
                .getString(LANGUAGE, null)
            return lang != null
        }

        fun configLanguage(context: Context): Context? {
            return setLocale(getUserLanguageCode(), context)
        }

        fun getUserLanguageCode(): String {
            return getLanguagesCodes()[getLanguageIndex(context)]
        }

        fun isEnglish(context: Context?): Boolean {
            return getUserLanguage(context) == ENGLISH
        }

        fun isArabic(context: Context?): Boolean {
            return getUserLanguage(context) == ARABIC
        }

        fun setLanguageEnglish(onSessionUpdate: () -> Unit) {
            setUserLanguageSession(ENGLISH, onSessionUpdate)
        }

        fun setLanguageArabic(onSessionUpdate: () -> Unit) {
            setUserLanguageSession(ARABIC, onSessionUpdate)
        }


        fun getLanguages(): List<String> {
            return Arrays.asList(ARABIC, ENGLISH)
        }

        private fun getLanguagesCodes(): List<String> {
            return Arrays.asList(AR, EN)
        }

        fun getLanguageIndex(context: Context?): Int {
            val index = getLanguages().indexOf(getUserLanguage(context))
            return if (index == -1) 1 else index
        }

        private fun setLocale(lang: String, context: Context): Context? {
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            //configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }


        interface SessionCallBack {
            fun setOnLogout()
        }

        interface OnSessionUpdate {
            fun refreshActivity()
        }
    }
