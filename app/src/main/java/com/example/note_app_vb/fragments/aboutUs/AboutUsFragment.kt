package com.example.note_app_vb.fragments.aboutUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.note_app_vb.NoteApplication.Companion.longToast
import com.example.note_app_vb.activities.home.sharedviewmodel.HomeActivityViewModel
import com.example.note_app_vb.databinding.FragmentAboutUsBinding


class AboutUsFragment : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding
    private val homeActivityViewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeActivityViewModel.isFabVisible.value = false
        initView()
    }


    class MyWebViewClient(private val progressBar: ProgressBar) : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            view?.loadUrl(url)
            progressBar.visibility = View.VISIBLE
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            progressBar.visibility = View.VISIBLE
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            progressBar.visibility = View.GONE
            longToast("Got Error! $error")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }
    }
    private fun initView(){
        val root: View = binding.root
        binding.apply {
            //We are passing progressBar to 'MyWebViewClient' as we can't access 'progressBar'
            // from Global access approach
            wvAboutus.webViewClient = MyWebViewClient(progressBar)
            wvAboutus.loadUrl("https://www.javatpoint.com/")
        }
    }
}