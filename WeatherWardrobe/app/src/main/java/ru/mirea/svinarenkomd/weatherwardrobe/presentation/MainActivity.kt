package ru.mirea.svinarenkomd.weatherwardrobe.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.databinding.ActivityMainBinding
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "MainActivity created")

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        ).get(MainViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)

        mainViewModel.actionStatusState.observe(this) { statusMsg ->
            if (statusMsg.isNotBlank()) {
                Toast.makeText(this, statusMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
