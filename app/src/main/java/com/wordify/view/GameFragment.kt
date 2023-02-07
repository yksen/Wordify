package com.wordify.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.wordify.R
import com.example.wordify.databinding.FragmentGameBinding
import com.wordify.viewmodel.GameViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by activityViewModels()
    private val mainScope = MainScope()

    private val boardSize = 4

    private var inputStarted: Boolean = false
    private var inputString: String = ""
    private var inputViews: MutableList<Button> = mutableListOf()

    private val baseColor by lazy { ContextCompat.getColor(requireContext(), R.color.purple_500) }
    private val activeColor by lazy { ContextCompat.getColor(requireContext(), R.color.orange) }
    private val correctColor by lazy { ContextCompat.getColor(requireContext(), R.color.green) }
    private val incorrectColor by lazy { ContextCompat.getColor(requireContext(), R.color.red) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateBoard(('a'..'z').toList())
        setupGameInput()

        viewModel.score.observe(viewLifecycleOwner) {
            binding.scoreText.text = it.toString()
        }
    }

    private fun populateBoard(letters: List<Char>) {
        binding.gameBoardLayout.children.forEach { button ->
            (button as Button).text = letters.random().toString()
        }
    }

    private fun setupGameInput() {
        binding.gameBoardLayout.children.forEach { child ->
            child.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.performClick()
                        startInput(v)
                    }
                    MotionEvent.ACTION_UP -> {
                        mainScope.launch { endInput() }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val pointerX = event.x + v.left
                        val pointerY = event.y + v.top
                        binding.gameBoardLayout.children.forEach {
                            val isUnderPointer =
                                it.left <= pointerX && pointerX <= it.right && it.top <= pointerY && pointerY <= it.bottom
                            if (isUnderPointer) {
                                if (inputStarted && !inputViews.contains(it) && (it as Button).isAdjacentTo(inputViews.last())) {
                                    addLetter(it)
                                } else if (inputStarted && inputViews.size > 1 && inputViews[inputViews.size - 2] == it) {
                                    undoLastLetter()
                                }
                            }
                        }
                    }
                }
                true
            }
        }
    }

    private fun startInput(v: View?) {
        (v as Button).setBackgroundColor(activeColor)
        inputStarted = true
        inputString = v.text.toString()
        inputViews = mutableListOf(v)
    }

    private suspend fun endInput() {
        inputStarted = false
        val isValid = viewModel.isValidWord(inputString)
        if (isValid) {
            inputViews.forEach { it.setBackgroundColor(correctColor) }
        } else {
            inputViews.forEach { it.setBackgroundColor(incorrectColor) }
        }
        delay(500)
        binding.gameBoardLayout.children.forEach { button ->
            (button as Button).setBackgroundColor(baseColor)
        }
    }

    private fun addLetter(it: Button) {
        it.setBackgroundColor(activeColor)
        inputString += it.text
        inputViews.add(it)
    }

    private fun undoLastLetter() {
        inputViews.last().setBackgroundColor(baseColor)
        inputString = inputString.dropLast(1)
        inputViews.removeLast()
    }

    private fun Button.isAdjacentTo(other: Button): Boolean {
        val thisIndex = binding.gameBoardLayout.indexOfChild(this)
        val otherIndex = binding.gameBoardLayout.indexOfChild(other)
        val thisRow = thisIndex / boardSize
        val thisCol = thisIndex % boardSize
        val otherRow = otherIndex / boardSize
        val otherCol = otherIndex % boardSize
        return (thisRow - otherRow).absoluteValue <= 1 && (thisCol - otherCol).absoluteValue <= 1
    }
}