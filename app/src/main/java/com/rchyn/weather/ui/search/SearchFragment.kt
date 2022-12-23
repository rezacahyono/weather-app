package com.rchyn.weather.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.rchyn.weather.adapter.ListLocationAdapter
import com.rchyn.weather.databinding.FragmentSearchBinding
import com.rchyn.weather.domain.model.location.RecentLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding as FragmentSearchBinding

    private lateinit var listLocationAdapter: ListLocationAdapter

    private val searchLocationViewModel: SearchLocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listLocationAdapter = ListLocationAdapter { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutSearchBar.apply {
            layoutSearchLocation.setEndIconOnClickListener {
                setupSearchLocation()
            }
            searchLocation.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    setupSearchLocation()
                }
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchLocationViewModel.searchLocationState.collect { state ->
                    when {
                        state.isLoading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }
                        state.isError -> {
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                        state.locationDatas.isNotEmpty() -> {
                            listLocationAdapter.submitList(state.locationDatas)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchLocationViewModel.recentLocationState.collect { state ->
                    if (!state.isError) {
                        setupChipRecentLocation(state.recentLocation)
                    } else {
                        Log.d("TAG", "Error")
                    }
                }

            }
        }

        binding.tvClearRecentLocation.setOnClickListener {
            searchLocationViewModel.deleteRecentLocation()
        }

        setupRecyclerLocation()
    }

    private fun setupSearchLocation() {
        val nameLocation = binding.layoutSearchBar.searchLocation.text.toString().trim()

        if (nameLocation.isNotEmpty()) {
            searchLocationViewModel.loadLocationByName(nameLocation)
            searchLocationViewModel.insertRecentLocation(RecentLocation(nameLocation))
        }
    }

    private fun setupChipRecentLocation(recents: List<RecentLocation>) {
        binding.cgRecentLocation.removeAllViews()
        recents.forEach {
            val chipRecent = Chip(requireContext())
            chipRecent.text = it.name
            binding.cgRecentLocation.addView(chipRecent)
        }
    }

    private fun setupRecyclerLocation() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLocation.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            adapter = listLocationAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchLocationViewModel.clearSearchLocationState()
        _binding = null
    }
}