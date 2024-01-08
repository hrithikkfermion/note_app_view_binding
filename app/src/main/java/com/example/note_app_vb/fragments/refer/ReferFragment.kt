package com.example.note_app_vb.fragments.refer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.R
import com.example.note_app_vb.activities.home.sharedviewmodel.HomeActivityViewModel
import com.example.note_app_vb.databinding.FragmentReferBinding
import com.example.note_app_vb.di.ActivityContext


class ReferFragment : Fragment() {
    private lateinit var binding: FragmentReferBinding
    private lateinit var viewModel: ReferViewModel
    private lateinit var factory: ReferViewModelFactory
    private val homeActivityViewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReferBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        homeActivityViewModel.isFabVisible.value = false
        val root: View = binding.root
        val activityContext = ActivityContext(root.context) //Getting Activity context
        factory = ReferViewModelFactory(activityContext, "9665494024")
        viewModel = ViewModelProvider(this, factory)[ReferViewModel::class.java]
        binding.apply {
            btnWhatsapp.setOnClickListener {
                viewModel.onWhatsAppShareClicked()
            }
            btnEmail.setOnClickListener {
                viewModel.sendEmail()
            }
            btnMsg.setOnClickListener {
                viewModel.shareTextMessageOnly()
            }
        }
    }
    //Added a comment


}