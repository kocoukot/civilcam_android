package com.civilcam.ui.terms.webView

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.databinding.FragmentWebviewBinding
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.requireArg
import com.civilcam.ext_features.viewBinding

class WebViewFragment : Fragment(R.layout.fragment_webview) {
    private val binding by viewBinding(FragmentWebviewBinding::bind)

    private val webLink by requireArg<String>(ARG_WEB_LINK)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener {
                navController.popBackStack()
            }

            title.text = webLink
            webView.apply {
//                settings.useWideViewPort = true
                settings.javaScriptEnabled = true
//                settings.domStorageEnabled = true
//                settings.databaseEnabled = true
//                settings.minimumFontSize = 1
//                settings.minimumLogicalFontSize = 1
//                settings.setSupportZoom(false)
//                settings.allowFileAccess = true
//                settings.allowContentAccess = true
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