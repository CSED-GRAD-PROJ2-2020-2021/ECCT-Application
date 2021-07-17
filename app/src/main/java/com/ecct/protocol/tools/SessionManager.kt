package com.ecct.protocol.tools
import android.content.Context
import android.content.SharedPreferences
import com.ecct.protocol.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val KEY = "key"
        const val ID = "id"
        const val IV = "iv"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }
    /**
     * Function to save key , id ,iv
     */
    fun saveKeyIdIv(key: String?, id: String?, iv:String?) {
        val editor = prefs.edit()
        editor.putString(KEY,key)
        editor.putString(ID,id)
        editor.putString(IV,iv)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    /**
     * Function to fetch key
     */
    fun fetchKey():String?{
        return prefs.getString(KEY,null)
    }
    /**
     * Function to fetch id
     */
    fun fetchID():String?{
        return prefs.getString(ID,null)
    }
    /**
     * Function to fetch key
     */
    fun fetchIv():String?{
        return prefs.getString(IV,null)
    }
}