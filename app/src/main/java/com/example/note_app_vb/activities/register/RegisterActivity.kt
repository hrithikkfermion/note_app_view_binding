package com.example.note_app_vb.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.activities.register.RegisterActivityViewModelFactory
import com.example.note_app_vb.NoteApplication.Companion.getDataStoreManager
import com.example.note_app_vb.R
import com.example.note_app_vb.activities.home.HomeActivity
import com.example.note_app_vb.activities.utils.filters.AlphabetInputFilter
import com.example.note_app_vb.database.NoteDatabase
import com.example.note_app_vb.databinding.ActivityRegisterBinding
import com.example.note_app_vb.local.UserRepository

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel: RegisterActivityViewModel
    private lateinit var factory : RegisterActivityViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }
    private fun initView(){
        val bundle: Bundle? = intent.extras
        val mobileNoData: String? = bundle?.getString("mobileNo")
        val userRepository = UserRepository(NoteDatabase.getDatabase(this))
        factory = RegisterActivityViewModelFactory(
            getDataStoreManager(this),
            userRepository,
            mobileNoData.toString()
        )
        viewModel = ViewModelProvider(this,factory)[RegisterActivityViewModel::class.java]
//        binding.registerViewModel = viewModel
//        binding.lifecycleOwner = this
        setupButtonClick(mobileNoData)

        //Below code for hiding toolbar which is not required
        val toolbar = supportActionBar
        toolbar!!.hide()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPress() //We need to terminate Activity manually
            }
        })

    }
    private fun setupButtonClick(mobileNoData: String?) {
        binding.apply {
            edtxtMobile.isEnabled = false //Disabling editing of mobile number
            edtxtMobile.setText(mobileNoData) //Setting up the entered mobile No
            btnRegister.setOnClickListener {
                viewModel.validateRegisterUser(
                    edtxtName.text.toString(),
                    edtxtEmailId.text.toString(),
                    edtxtPwd.text.toString(),
                    edtxtConfirmPwd.text.toString()
                )
            }
            //Here we are setting up "alphabet" filter for username field.
            val filters = arrayOf<InputFilter>(AlphabetInputFilter())
            edtxtName.filters = filters

            ivExit.setOnClickListener {
                onBackPress()
            }
        }

        viewModel.isRegistrationStatus.observe(this) { registrationStatus ->
            when(registrationStatus){
                //Empty indicates no registration error msg returned so navigate to HomeActivity.
                "" -> goToHome()
            }
        }
    }
    private fun goToHome(){
        val myIntent = Intent(this, HomeActivity::class.java)
        this.startActivity(myIntent)
        finish()
    }

    private fun onBackPress() {
        finish()
        finishAffinity() //Removes all activities in backstack
    }
}