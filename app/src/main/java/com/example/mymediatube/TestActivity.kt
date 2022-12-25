package com.example.mymediatube

import android.Manifest.permission.GET_ACCOUNTS
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.lifecycle.ViewModelProvider
import com.example.mymediatube.databinding.ActivityTestBinding
import com.example.mymediatube.helper.NetworkHelper
import com.example.mymediatube.helper.PermissionsHelper
import com.example.mymediatube.viewmodels.TestViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes

class TestActivity : AppCompatActivity(), PermissionsHelper.PermissionCallbacks {

    private var _credential: GoogleAccountCredential? = null
    private val credential get() = _credential!!

    private var _binding: ActivityTestBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: TestViewModel? = null
    private val viewModel get() = _viewModel!!

    private val scopes = listOf(YouTubeScopes.YOUTUBE_READONLY)

    private val REQUEST_PERMISSION_GET_ACCOUNTS = 1003

    private val PREF_ACCOUNT_NAME = "accountName"

    private val accountPicker = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            TODO("Not yet implemented")
        }
    }
    private val googleResolver = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            TODO("Not yet implemented")
        }
    }
    private val networkHelper = NetworkHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel = ViewModelProvider(this)[TestViewModel::class.java]

        binding.callButton.setOnClickListener {
            it.isEnabled = false
            binding.textView.text = ""
            // getResultsFromApi()
            viewModel.loadSearchDataFromApi()
            it.isEnabled = true
        }

        binding.textView.text = getString(
            R.string.instruction_text, binding.callButton.text)

        _credential = GoogleAccountCredential.usingOAuth2(
            applicationContext, scopes)
            .setBackOff(ExponentialBackOff())

        viewModel.channelData.observe(this) {
            binding.textView.text = TextUtils.join(
                "\n", listOf("Data retrieved using the YouTube Data API:") + it)
        }
        viewModel.searchData.observe(this) {
            binding.textView.text = TextUtils.join(
                "\n", listOf("Data retrieved using the YouTube Data API:") + it)
        }
    }

    private fun getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices()
        } else if (credential.selectedAccountName == null) {
            chooseAccount()
        } else if (!isDeviceOnline()) {
            binding.textView.text = getString(R.string.network_error)
        } else {
            viewModel.loadChannelDataFromApi(credential)
        }
    }

    private fun isDeviceOnline() = networkHelper.checkConnection(this)

    private fun chooseAccount() {
        if (PermissionsHelper.hasPermissions(this, GET_ACCOUNTS)) {
            val accountName = getPreferences(Context.MODE_PRIVATE)
                .getString(PREF_ACCOUNT_NAME, null)
            if (accountName != null) {
                credential.setSelectedAccountName(accountName)
                getResultsFromApi()
            } else {
                accountPicker.launch(
                    credential.newChooseAccountIntent())
            }
        } else {
            PermissionsHelper.requestPermissions(
                this,
                "This app needs to access your Google account (via Contacts).",
                REQUEST_PERMISSION_GET_ACCOUNTS,
                GET_ACCOUNTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsHelper.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this)
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    private fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        googleResolver.launch(apiAvailability.getErrorResolutionIntent(
            this, connectionStatusCode, null))
    }


    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        super.onStart()
        networkHelper.registerCallback(this)
    }

    override fun onStop() {
        super.onStop()
        networkHelper.unregisterCallback(this)
    }

}