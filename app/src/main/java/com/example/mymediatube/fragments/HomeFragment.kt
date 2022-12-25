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
import com.example.mymediatube.viewmodels.ConnectionViewModel
import com.example.mymediatube.viewmodels.HomeViewModel

private const val TAG = "HomeFragment"
class HomeFragment: Fragment(), MenuProvider {

    private val viewModel: HomeViewModel by viewModels()
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
            val hasPending = viewModel.isLoadPending
            val wasConnected = connectedViewModel.isConnected
            val connected = connectedViewModel.checkConnection(requireContext())
            if (connected) {
                // So that it does not load after loading from the connected observer
                if (wasConnected && !hasPending) {
                    viewModel.loadHomeData(false)
                }
            } else {
                viewModel.loadHomeData(true)
            }
        }
        viewModel.homeData.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        }
        connectedViewModel.isConnectedData.observe(viewLifecycleOwner) {
            if (it) viewModel.loadIfPending()
        }
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