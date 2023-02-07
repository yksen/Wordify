package com.wordify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wordify.model.Word
import com.wordify.repository.WordRepository
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WordRepository()

    private val _wordsFound = MutableLiveData<List<String>>()
    val wordsFound: LiveData<List<String>> = _wordsFound

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private fun handleWordResponse(response: Response<ResponseBody>): ResponseBody {
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        return response.errorBody()!!
    }

    suspend fun isValidWord(input: String): Boolean =
        withContext(viewModelScope.coroutineContext) {
            val response = repository.getWord(input)
            val responseBody = handleWordResponse(response)
            if (responseBody.string().contains("No Definitions Found")) {
                false
            } else {
                var word = Gson().fromJson(responseBody.string(), Word::class.java)
                true
            }
        }

}