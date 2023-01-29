package com.example.mymediatube.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mymediatube.R
import com.example.mymediatube.adapters.SearchAdapter
import com.example.mymediatube.databinding.FragmentHomeBinding
import com.example.mymediatube.ext.activityCompat
import com.example.mymediatube.ext.app
import com.example.mymediatube.viewmodels.ConnectionViewModel
import com.example.mymediatube.viewmodels.HomeViewModel

private const val TAG = "HomeFragment"
class HomeFragment: Fragment(), MenuProvider {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.Factory(app.dataRepository)
    }
    private val connectedViewModel: ConnectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(
            inflater, container, false)

        activityCompat.setSupportActionBar(binding.toolbar)
        // Calling with viewLifecycleOwner does not remove the
        // provider on onDestroyView()
        activityCompat.addMenuProvider(this)

        val adapter = SearchAdapter {}
        binding.recyclerView.adapter = adapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadHomeData()
        }
        viewModel.showRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it == true
        }
        viewModel.homeData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.loadIfNotLoaded()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activityCompat.removeMenuProvider(this)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.search -> {
                val action = HomeFragmentDirections.startSearch()
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }
}