package com.andruid.magic.dailytasks.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.data.model.User
import com.andruid.magic.dailytasks.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavHostActivity : AppCompatActivity() {
    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }
    private val viewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen().apply {
            setKeepOnScreenCondition { true }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_host)
        supportActionBar?.hide()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getCurrentUser().collectLatest {
                    val (graphId, rootLayouts) = getNavigationConfig(it)
                    navController.graph = navController.navInflater.inflate(graphId)
                    setupActionBarWithNavController(
                        navController,
                        AppBarConfiguration(rootLayouts)
                    )
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signupFragment, R.id.homeFragment -> {
                    splashScreen.setKeepOnScreenCondition { false }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun getNavigationConfig(user: User?) = user?.let {
        R.navigation.navigation_main to setOf(R.id.homeFragment)
    } ?: R.navigation.navigation_auth to setOf(
        R.id.signupFragment,
        R.id.selectAvatarBottomSheetDialogFragment
    )
}