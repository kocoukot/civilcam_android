package com.civilcam.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.civilcam.databinding.ActivityMainBinding
import com.standartmedia.common.ext.castSafe

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navHost: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ActivityMainBinding.inflate(layoutInflater)
                .also { binding = it }
                .root
        )

        navHost = supportFragmentManager.findFragmentById(binding.navHostView.id)
            .castSafe<NavHostFragment>()

        navHost?.apply {
            childFragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)
        }

        navHost?.let { nav ->
//            binding.bottomNavigationView.setupWithNavController(
//                navController = nav.navController,
//            )
        }
    }

    private val onBackStackChangedListener by lazy {
        FragmentManager.OnBackStackChangedListener {
//            binding.bottomNavigationView.isVisible = currentVisibleFragment is SupportBottomBar
        }
    }


}