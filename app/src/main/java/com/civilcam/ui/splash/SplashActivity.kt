package com.civilcam.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.databinding.ActivitySplashBinding
import com.civilcam.ui.MainActivity
import com.civilcam.ui.common.NavigationDirection
import com.civilcam.ui.common.ext.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModel<SplashViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ActivitySplashBinding.inflate(layoutInflater)
                .also { binding = it }
                .root
        )
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToTargetActivity()
        }, 2000)
        observeLiveData()
    }

    private fun navigateToTargetActivity(direction: NavigationDirection? = null) {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                if (direction != null) putExtra(MainActivity.EXTRA_DIRECTION, direction)
            }
        )
        finish()
    }
    
    private fun observeLiveData() = with(viewModel) {
        user.observeNonNull(this@SplashActivity) { user ->
            user?.let {
                navigateToTargetActivity(NavigationDirection.resolveDirectionFor(it))
            }
        }
    }
}