package com.civilcam.ui.profile.credentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.ui.common.ext.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeCredentialsFragment : Fragment() {
	private val viewModel: ChangeCredentialsViewModel by viewModel()
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
			
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			
			setContent {
				ChangeCredentialsScreenContent(viewModel)
			}
		}
	}
}