package com.civilcam.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.civilcam.databinding.ActivitySplashBinding
import com.civilcam.ui.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

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
    }

    private fun navigateToTargetActivity() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
        finish()
    }
}