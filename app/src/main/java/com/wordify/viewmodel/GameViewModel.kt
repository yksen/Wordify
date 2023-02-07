package com.wordify.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wordify.model.Profile
import com.wordify.model.Word
import com.wordify.repository.WordRepository
import com.wordify.util.getPlayerProfile
import com.wordify.util.savePlayerProfile
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WordRepository()

    var playerProfile: Profile = getPlayerProfile(application)

    private var countDownTimer: CountDownTimer? = null
    private val gameTime = 2 * 60 * 1000L
    private val interval = 1000L

    private val _gameFinished: MutableLiveData<Boolean> = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> = _gameFinished

    private val _timeRemaining: MutableLiveData<Long> = MutableLiveData(gameTime)
    val timeRemaining: LiveData<Long> = _timeRemaining

    private val _wordsFound: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val wordsFound: LiveData<List<String>> = _wordsFound

    private val _score: MutableLiveData<Int> = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private fun handleWordResponse(response: Response<ResponseBody>): ResponseBody {
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        return response.errorBody()!!
    }

    suspend fun isValidWord(input: String): Boolean =
        if (input.length >= 3) {
            withContext(viewModelScope.coroutineContext) {
                val response = repository.getWord(input)
                val responseBody = handleWordResponse(response)
                val json = responseBody.string()
                val isError = json.contains("No Definitions Found")
                if (isError) {
                    false
                } else {
                    if (!wordsFound.value!!.contains(input)) {
                        val word = Gson().fromJson(json, Word::class.java)
                        _wordsFound.value = _wordsFound.value?.plus(word.first().word)
                        updateScore(word.first().word)
                    }
                    true
                }
            }
        } else {
            false
        }

    private fun updateScore(word: String) {
        _score.value = _score.value?.plus((word.length - 2) * (word.length - 2))
    }

    fun startTimer() {
        _gameFinished.value = false
        countDownTimer = object : CountDownTimer(gameTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                _timeRemaining.value = millisUntilFinished
            }

            override fun onFinish() {
                endGame()
            }

        }.start()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
    }

    fun endGame() {
        playerProfile.points += score.value!!
        playerProfile.gamesPlayed++
        playerProfile.wordsFound += wordsFound.value?.size ?: 0
        savePlayerProfile(getApplication(), playerProfile)
        _gameFinished.value = true
    }

    fun reset()
    {
        stopTimer()
        _gameFinished.value = false
        _timeRemaining.value = gameTime
        _wordsFound.value = listOf()
        _score.value = 0
    }
}