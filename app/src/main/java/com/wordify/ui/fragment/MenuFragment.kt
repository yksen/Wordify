package com.wordify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.wordify.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.playButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToGameFragment()
            it.findNavController().navigate(action)
        }

        binding.statsButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToStatsFragment()
            it.findNavController().navigate(action)
        }

        return binding.root
    }
}