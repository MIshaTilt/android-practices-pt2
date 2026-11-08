package ru.mirea.svinarenkomd.fragmentmanagerapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ShareViewModel::class.java)
        viewModel.getSelectedItem().observe(this) { item ->
            Log.d(MainActivity::class.java.simpleName, "Selected country: $item")
        }

        val headerFragment = HeaderFragment.newInstance("MIREA")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.headerContainerView, headerFragment, "header")
                .add(R.id.detailsContainerView, DetailsFragment(), "details")
                .commit()
        }
    }
}
