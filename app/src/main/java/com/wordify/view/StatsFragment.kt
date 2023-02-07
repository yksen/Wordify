package com.wordify.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.wordify.databinding.FragmentStatsBinding
import com.wordify.viewmodel.GameViewModel

class StatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.text = viewModel.playerProfile.name
        binding.points.text = String.format("Points: %d", viewModel.playerProfile.points)
        binding.wordsFound.text = String.format("Words found: %d", viewModel.playerProfile.wordsFound)
        binding.gamesPlayed.text = String.format("Games played: %d", viewModel.playerProfile.gamesPlayed)
    }
}