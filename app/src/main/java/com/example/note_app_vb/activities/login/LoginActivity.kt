package com.example.note_app_vb.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.NoteApplication.Companion.getDataStoreManager
import com.example.note_app_vb.R
import com.example.note_app_vb.activities.main.MainActivity
import com.example.note_app_vb.activities.register.RegisterActivity
import com.example.note_app_vb.database.NoteDatabase
import com.example.note_app_vb.databinding.ActivityLoginBinding
import com.example.note_app_vb.local.UserRepository

class LoginActivity : AppCompatActivity() {
    //We are making the userId Global as we require it's value in another function
    var userId: Long = -1 //Default value setting as '-1' for non registered user

    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel
    lateinit var factory: LoginActivityViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
//Passing instance of Shared preference which is required in LoginViewModel
        //Passing context required for toast message and other operations in LoginActivity
        //Here we are passing DB instance to 'UserRepository'
        val userRepository = UserRepository(NoteDatabase.getDatabase(this))
        //Here we are passing userRepository for db queries access.
        factory = LoginActivityViewModelFactory(
            getDataStoreManager(this),
            userRepository
        )
        viewModel = ViewModelProvider(this, factory)[LoginActivityViewModel::class.java]
//        binding.loginViewModel = viewModel
//        binding.lifecycleOwner = this
        setupButtonClick()

        //Below code for hiding toolbar which is not required
        val toolbar = supportActionBar
        toolbar!!.hide()
    }

    private fun setupButtonClick() {

        binding.apply {
            btnLogin.setOnClickListener {
                viewModel.validateLoginUser(
                    binding.edtxtMobileLogin.text.toString(),
                    binding.edtxtPwd.text.toString()
                )
            }
            edtxtPwd.visibility =
                View.GONE //Setting initially password field invisible for fresh users.
            ivExit.setOnClickListener {
                onBackPress()
            }
        }

        //Below logic not required as context based Toast msg & Activity redirection
        // can be done in LoginViewModel class.
        viewModel.isLoginStatus.observe(this) { loginStatus ->
            when (loginStatus) {
                //Empty indicates no login error msg returned so navigate to HomeActivity
                "" -> {
                    goToHome()
                }
            }
        }

        viewModel.isUserExistAndRegistered.observe(this) { regStatus ->
            when (regStatus) {
                "0" -> {
                    goToRegister()
//                    Toast.makeText(this, "Register User", Toast.LENGTH_SHORT).show()
                }

                "1" -> {
                    //Making password field visible for user to type password.
                    binding.edtxtPwd.visibility =
                        View.VISIBLE //This has to be done from Activity only
                }
            }
        }
    }

    private fun goToRegister() {
        val myIntent = Intent(this, RegisterActivity::class.java)
        myIntent.putExtra(
            "mobileNo",
            binding.edtxtMobileLogin.text.toString()
        ) //Optional parameters
        startActivity(myIntent)
        viewModel.isUserExistAndRegistered.value = "" //Resetting
    }
    private fun goToHome(){
        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
        finish()
    }

    private fun onBackPress() {
        finish()
        finishAffinity() //Removes all activities in backstack
    }
}