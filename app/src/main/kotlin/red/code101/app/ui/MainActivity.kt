package red.code101.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.R
import red.code101.app.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val controller = navHostFragment.navController
        lifecycleScope.launchWhenStarted {
            controller.currentBackStackEntryFlow.collect {
                binding.cmd.text = controller.backQueue.run {
                    if (isEmpty()) return@run "empty"
                    joinToString {
                        "\n" + it.destination.label
                    }
                }
            }
        }

        setContentView(binding.root)
    }
}