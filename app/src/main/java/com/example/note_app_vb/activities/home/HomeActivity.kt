package com.example.note_app_vb.activities.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.note_app_vb.NoteApplication.Companion.shortToast
import com.example.note_app_vb.R
import com.example.note_app_vb.activities.home.sharedviewmodel.HomeActivityViewModel
import com.example.note_app_vb.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var navController : NavController
    private var doubleBackToExitPressedOnce = false
    private val homeActivityViewModel by viewModels<HomeActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPress() //We need to terminate Activity manually
            }
        })
    }

    private fun initView() {
        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)
                        as NavHostFragment
            navController = navHostFragment.navController
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment, R.id.referFragment, R.id.aboutUsFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            //Below code we can select any fragment we want
            navView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.homeFragment -> {
                        println("Clicked at Home Fragment")
                        selectBottomViewFragmentNavigation(0)
                        true
                    }

                    R.id.referFragment -> {
                        println("Clicked at Refer Fragment")
                        selectBottomViewFragmentNavigation(1)
                        true
                    }

                    R.id.aboutUsFragment -> {
                        println("Clicked at About Us Fragment")
                        selectBottomViewFragmentNavigation(2)
                        true
                    }

                    else -> false
                }
            }
            fabAddNote.setOnClickListener {
                homeActivityViewModel.addNote()
            }
        }
        homeActivityViewModel.isFabVisible.observe(this) { isFabVisible ->
            when (isFabVisible) {
                true -> binding.fabAddNote.show()
                false -> binding.fabAddNote.hide()
            }

        }
    }

    private fun selectBottomViewFragmentNavigation(clickedIndexPos : Int){
        if(clickedIndexPos == 0){

            if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_refer)){
                navController.navigate(R.id.action_referFragment_to_homeFragment)
            }
            else if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_aboutus)){
                navController.navigate(R.id.action_aboutUsFragment_to_homeFragment)
            }
        }
        else if(clickedIndexPos == 1){
            if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_home)) {
                navController.navigate(R.id.action_homeFragment_to_referFragment)
            }
            else if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_aboutus)) {
                navController.navigate(R.id.action_aboutUsFragment_to_referFragment)
            }
        }
        else if(clickedIndexPos == 2){
            if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_home)) {
                navController.navigate(R.id.action_homeFragment_to_aboutUsFragment)
            }
            else if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_refer)) {
                navController.navigate(R.id.action_referFragment_to_aboutUsFragment)
            }
        }
    }
    fun onBackPress() {
        if (doubleBackToExitPressedOnce) {
            finish()
            finishAffinity() //Removes all activities in backstack
        }

        this.doubleBackToExitPressedOnce = true
        shortToast(resources.getString(R.string.press_back_again_exit))

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)// Delay of 2 seconds to allow for the second back press
    }
}