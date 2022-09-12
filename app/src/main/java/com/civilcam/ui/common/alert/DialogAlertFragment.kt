package com.civilcam.ui.common.alert


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
import com.civilcam.ext_features.AlertDialogTypes
import com.civilcam.ext_features.compose.elements.AlertDialogComp


class DialogAlertFragment(
    private val dialogTitle: String,
    private val dialogText: String,
    private val alertType: AlertDialogTypes
) :
    DialogFragment() {
    private var onItemSelected: ((Boolean) -> Unit)? = null

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
            AlertDialogComp(
                dialogTitle = dialogTitle,
                dialogText = dialogText,
                alertType,
            )
            { answer ->
                onItemSelected?.invoke(answer)
                this@DialogAlertFragment.dismiss()
            }
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
            text: String,
            alertType: AlertDialogTypes,
            onOptionSelected: ((Boolean) -> Unit)? = null
        ) {
            with(fragmentManager) {
                DialogAlertFragment("", text, alertType)
                    .apply { onItemSelected = onOptionSelected }
                    .show(this, TAG)
            }
        }

        fun create(
            fragmentManager: FragmentManager,
            title: String = "",
            text: String,
            alertType: AlertDialogTypes,
            onOptionSelected: ((Boolean) -> Unit)? = null
        ) {
            with(fragmentManager) {
                DialogAlertFragment(title, text, alertType)
                    .apply { onItemSelected = onOptionSelected }
                    .show(this, TAG)
            }
        }
    }
}
