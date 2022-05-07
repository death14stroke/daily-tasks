package com.andruid.magic.dailytasks.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.repository.ProfileRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            ProfileRepository.getUser().firstOrNull()?.let {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } ?: run {
                startActivity(Intent(this@SplashActivity, SignupActivity::class.java))
            }

            finish()
        }
    }
}