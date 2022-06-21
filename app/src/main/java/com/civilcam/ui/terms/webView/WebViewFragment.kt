package com.civilcam.ui.terms.webView

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.databinding.FragmentWebviewBinding
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.common.ext.viewBinding

class WebViewFragment : Fragment(R.layout.fragment_webview) {
    private val binding by viewBinding(FragmentWebviewBinding::bind)

    private val webLink by requireArg<String>(ARG_WEB_LINK)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener {
                navController.popBackStack()
            }

            Toast.makeText(
                requireContext(),
                "Web page will be when api will be integrated",
                Toast.LENGTH_SHORT
            ).show()
            title.text = "Linkhere.com"
            webView.apply {
                settings.useWideViewPort = true
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.databaseEnabled = true
                settings.minimumFontSize = 1
                settings.minimumLogicalFontSize = 1
                settings.setSupportZoom(false)
                settings.allowFileAccess = true
                settings.allowContentAccess = true
                loadUrl(webLink)
            }
        }

    }

    companion object {
        private const val ARG_WEB_LINK = "web_link"

        fun createArgs(webLink: String) = bundleOf(
            ARG_WEB_LINK to webLink
        )
    }
}