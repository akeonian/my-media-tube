package com.example.mymediatube

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mymediatube.databinding.ActivityMainBinding
import com.example.mymediatube.viewmodels.ConnectionViewModel

class MainActivity: AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var viewModel: ConnectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup navController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        hideNavBarIfSearch(binding)

        // setup ConnectionViewModel
        viewModel = ViewModelProvider(this)[ConnectionViewModel::class.java]
        viewModel.isConnectedData.observe(this) {
            val visibility = if (it) View.GONE else View.VISIBLE
            binding.connectionMessage.visibility = visibility
        }
        viewModel.checkConnection(this)

        handleIntent(intent)
    }

    private fun hideNavBarIfSearch(binding: ActivityMainBinding) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val visibility = if (destination.id == R.id.searchFragment) View.GONE else View.VISIBLE
            binding.bottomNavigation.visibility = visibility
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("TAG", "onNewIntent()")
        intent?.also { handleIntent(intent) }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Call this only after initializing navController
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            query?.also {
                val action = MainDirections.showSearchResult(it)
                navController.navigate(action)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startObservingConnection(this)
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopObservingConnection(this)
    }
}