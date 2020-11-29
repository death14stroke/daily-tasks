package com.andruid.magic.dailytasks.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
                Log.d("splashLog", "user not null: ${it.userName}")
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } ?: run {
                Log.d("splashLog", "user null")
                startActivity(Intent(this@SplashActivity, SignupActivity::class.java))
            }

            finish()
        }
    }
}