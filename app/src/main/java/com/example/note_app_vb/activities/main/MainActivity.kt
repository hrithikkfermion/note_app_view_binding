package com.example.note_app_vb.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.R
import com.example.note_app_vb.NoteApplication.Companion.getDataStoreManager
import com.example.note_app_vb.activities.home.HomeActivity
import com.example.note_app_vb.activities.login.LoginActivity
import com.example.note_app_vb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    lateinit var factory: MainActivityViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition {
            true
        }
        setUpViewModel()
    }

    private fun setUpViewModel() {
        factory = MainActivityViewModelFactory(getDataStoreManager(this))
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        viewModel.isUserLoggedIn.observe(this) { loggedInStatus ->
            //using 'when'
            when (loggedInStatus) {
                true -> {
                    goToHome()
                }
                false -> goToLogin()
            }
        }
    }

    private fun goToHome() {
        val myIntent = Intent(this, HomeActivity::class.java)
        this.startActivity(myIntent)
        finish()
    }

    private fun goToLogin() {
        val myIntent = Intent(this, LoginActivity::class.java)
        this.startActivity(myIntent)
        finish()
    }
}