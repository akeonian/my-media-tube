package com.example.mymediatube.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.mymediatube.R
import com.example.mymediatube.adapters.SearchAdapter
import com.example.mymediatube.databinding.FragmentSearchResultBinding
import com.example.mymediatube.ext.activityCompat
import com.example.mymediatube.ext.app
import com.example.mymediatube.viewmodels.ConnectionViewModel
import com.example.mymediatube.viewmodels.SearchResultViewModel

private const val TAG = "SearchResultFragment"
class SearchResultFragment: Fragment(), MenuProvider {

    private val navigationArgs: SearchResultFragmentArgs by navArgs()
    private val viewModel: SearchResultViewModel by viewModels() {
        SearchResultViewModel.Factory(app.dataRepository)
    }
    private val connectionViewModel: ConnectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchResultBinding.inflate(
            inflater, container, false)

        binding.toolbar.title = navigationArgs.query

        activityCompat.setSupportActionBar(binding.toolbar)
        activityCompat.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activityCompat.addMenuProvider(this)

        val adapter = SearchAdapter {}
        binding.recyclerView.adapter = adapter
        viewModel.searchData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.performSearch()
        }
        viewModel.showRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it == true
        }
        connectionViewModel.isConnectedData.observe(viewLifecycleOwner) {
            if (it) viewModel.performSearch(navigationArgs.query)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activityCompat.removeMenuProvider(this)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_result, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        Log.d(TAG, "onMenuItemSelected()")
        // TODO("Not yet implemented")
        return false
    }
}