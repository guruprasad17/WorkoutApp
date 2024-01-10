package myapplication.WorkoutApp

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
    private val KEY_USER_ID = "USER_ID"
    private val KEY_PRODUCT_ID= "PRODUCT_ID"

    // Check if the user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Log in the user and store their ID
    fun login(userId: Int) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putInt(KEY_USER_ID, userId)
        editor.apply()
    }

    fun branchData(productId: Int)
    {
        editor.putInt(KEY_PRODUCT_ID,productId)
        editor.apply()
    }

    // Log out the user and clear their ID
    fun logout() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.remove(KEY_USER_ID)
        editor.apply()
    }

    fun getProductId(): Int{
        return sharedPreferences.getInt(KEY_PRODUCT_ID, 0)
    }

    // Retrieve the user's ID
    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 0) // Provide a default value if the ID is not found
    }
}