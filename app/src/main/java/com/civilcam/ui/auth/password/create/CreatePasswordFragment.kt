package com.civilcam.ui.auth.password.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.auth.password.create.model.CreatePasswordRoute
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePasswordFragment : Fragment() {
	private val viewModel: CreatePasswordViewModel by viewModel()
	
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
	
}