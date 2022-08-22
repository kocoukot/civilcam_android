package com.civilcam.ui.auth.password.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.auth.password.create.model.CreatePasswordRoute
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CreatePasswordFragment : Fragment() {
	private val viewModel: CreatePasswordViewModel by viewModel {
		parametersOf(token)
	}
	
	private val token: String by requireArg(ARG_TOKEN)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				CreatePasswordRoute.GoBack -> navController.popBackStack()
				CreatePasswordRoute.GoSave -> navController.popBackStack(R.id.loginFragment, false)
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				CreatePasswordScreenContent(viewModel)
			}
		}
	}
	
	companion object {
		private const val ARG_TOKEN = "token"
		
		fun createArgs(token: String) = bundleOf(
			ARG_TOKEN to token
		)
	}
	
}