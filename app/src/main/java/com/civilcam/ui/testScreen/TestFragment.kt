package com.civilcam.ui.testScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.databinding.FragmentTestBinding
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.viewBinding
import com.civilcam.ui.terms.TermsFragment

class TestFragment : Fragment(R.layout.fragment_test) {
    private val binding by viewBinding(FragmentTestBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            terms.setOnClickListener {
                navController.navigate(R.id.termsFragment, TermsFragment.createArgs(false))
            }
            profileSetup.setOnClickListener {
                navController.navigate(R.id.profileSetupFragment)
            }
            navBar.setOnClickListener {
                navController.navigate(R.id.network_root)
            }
        }
    }
}