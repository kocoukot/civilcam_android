package com.civilcam.ui.common.loading


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.civilcam.domainLayer.castSafe
import com.civilcam.ext_features.compose.elements.DialogLoadingContent


class DialogLoadingFragment :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        // Dispose the Composition when viewLifecycleOwner is destroyed
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
        )
        setContent {
            DialogLoadingContent()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setStyle(STYLE_NO_FRAME, android.R.style.Theme_Black)

        }

    }

    companion object {
        private const val TAG = "fragment_loading"

        fun create(
            fragmentManager: FragmentManager,
            isLoading: Boolean
        ) {
            with(fragmentManager) {
                if (isLoading) {
                    DialogLoadingFragment()
                        .show(this, TAG)
                } else {
                    findFragmentByTag(TAG)?.castSafe<DialogLoadingFragment>()?.dismiss()
                }


            }
        }
    }
}
