package com.civilcam.ui.auth.pincode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.ui.auth.pincode.model.PinCodeRoute
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PinCodeFragment : Fragment() {
	private val viewModel: PinCodeViewModel by viewModel() {
		parametersOf(isConfirm)
	}
	
	private val isConfirm: Boolean by requireArg(ARG_CONFIRM)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				PinCodeRoute.GoBack -> navController.popBackStack()
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				PinCodeScreenContent(viewModel)
			}
		}
	}
	
	companion object {
		private const val ARG_CONFIRM = "confirm"
		
		fun createArgs(isConfirm: String) = bundleOf(
			ARG_CONFIRM to isConfirm
		)
	}
}