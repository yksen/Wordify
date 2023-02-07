package com.wordify.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wordify.model.Profile

private const val PROFILE = "profile"
private const val PROFILE_FILE = "profiles_file"

fun savePlayerProfile(context: Context, list: Profile) {
    val json = Gson().toJson(list)
    val sharedPreferences = context.getSharedPreferences(PROFILE_FILE, Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(PROFILE, json).apply()
}

fun getPlayerProfile(context: Context): Profile {
    val sharedPreferences = context.getSharedPreferences(PROFILE_FILE, Context.MODE_PRIVATE)
    val json = sharedPreferences.getString(PROFILE, null)

    if (json.isNullOrEmpty()) {
        return Profile()
    }

    val type = object : TypeToken<Profile>() {}.type
    return Gson().fromJson(json, type)
}