package com.example.mymediatube.fragments

import android.app.SearchManager
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.mymediatube.databinding.FragmentSearchBinding
import com.example.mymediatube.ext.activityCompat
import com.example.mymediatube.viewmodels.ConnectionViewModel
import com.example.mymediatube.viewmodels.SearchViewModel

private const val TAG = "SearchFragment"
// The SearchView should be in the layout of the searchable activity
class SearchFragment: Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private val connectionViewModel: ConnectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(
            inflater, container, false);

        val searchManager = getSystemService(
            requireContext(), SearchManager::class.java)
        val searchableInfo = searchManager?.getSearchableInfo(activity?.componentName)
        binding.searchView.setSearchableInfo(searchableInfo)

        binding.backButton.setOnClickListener {
            activityCompat.onSupportNavigateUp()
        }
        binding.searchView.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return when (newText) {
                    null -> false
                    else -> {
                        viewModel.searchSuggestions(
                            !connectionViewModel.isConnected, newText)
                        true
                    }
                }
            }

        })
        val adapter = ArrayAdapter<String> (
            requireContext(), android.R.layout.simple_list_item_1)
        binding.listView.adapter = adapter
        viewModel.suggestions.observe(viewLifecycleOwner) {
            adapter.clear()
            adapter.addAll(it)
        }
        connectionViewModel.isConnectedData.observe(viewLifecycleOwner) {
            if (it) viewModel.loadIfPending()
        }
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position)?.let {
                binding.searchView.setQuery(it, true)
            }
        }
        return binding.root
    }

}