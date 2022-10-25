package com.civilcam.langselect

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import com.civilcam.ext_features.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LanguageSelectFragment : BaseFragment<LanguageSelectViewModel>() {
    override val viewModel: LanguageSelectViewModel by viewModel()
    override val screenContent: @Composable (LanguageSelectViewModel) -> Unit =
        { LanguageSelectScreenContent(viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window!!, true)
    }
}