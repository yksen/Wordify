package com.wordify.repository

import com.wordify.api.RetrofitInstance

class WordRepository {
    suspend fun getWord(word: String) = RetrofitInstance.api.getWord(word)
}