package ru.mirea.svinarenkomd.weatherwardrobe.presentation

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog.CatalogFragment
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.details.ClothingDetailsFragment
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.profile.ProfileFragment
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.scanner.AiScannerFragment
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.wardrobe.WardrobeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MainActivity created")

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        ).get(MainViewModel::class.java)

        val tabCatalog = findViewById<LinearLayout>(R.id.tabCatalog)
        val tabWardrobe = findViewById<LinearLayout>(R.id.tabWardrobe)
        val tabScanner = findViewById<LinearLayout>(R.id.tabScanner)
        val tabProfile = findViewById<LinearLayout>(R.id.tabProfile)

        mainViewModel.actionStatusState.observe(this) { statusMsg ->
            if (statusMsg.isNotBlank()) {
                Toast.makeText(this, statusMsg, Toast.LENGTH_SHORT).show()
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, CatalogFragment(), TAG_CATALOG)
                .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            syncActiveTabWithCurrentFragment()
        }

        tabCatalog.setOnClickListener {
            navigateToFragment(CatalogFragment(), TAG_CATALOG)
            highlightTab(0)
        }

        tabWardrobe.setOnClickListener {
            navigateToFragment(WardrobeFragment(), TAG_WARDROBE)
            highlightTab(1)
        }

        tabScanner.setOnClickListener {
            navigateToFragment(AiScannerFragment(), TAG_SCANNER)
            highlightTab(2)
        }

        tabProfile.setOnClickListener {
            navigateToFragment(ProfileFragment(), TAG_PROFILE)
            highlightTab(3)
        }

        syncActiveTabWithCurrentFragment()
    }

    private fun navigateToFragment(fragment: Fragment, tag: String) {
        val current = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        if (current != null && current::class == fragment::class) return

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainerView, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun syncActiveTabWithCurrentFragment() {
        val current = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        when (current) {
            is CatalogFragment, is ClothingDetailsFragment -> highlightTab(0)
            is WardrobeFragment -> highlightTab(1)
            is AiScannerFragment -> highlightTab(2)
            is ProfileFragment -> highlightTab(3)
        }
    }

    private fun highlightTab(activeIndex: Int) {
        val tabs = listOf(
            Triple(
                findViewById<LinearLayout>(R.id.tabCatalog),
                findViewById<ImageView>(R.id.iconTabCatalog),
                findViewById<TextView>(R.id.textTabCatalog)
            ),
            Triple(
                findViewById<LinearLayout>(R.id.tabWardrobe),
                findViewById<ImageView>(R.id.iconTabWardrobe),
                findViewById<TextView>(R.id.textTabWardrobe)
            ),
            Triple(
                findViewById<LinearLayout>(R.id.tabScanner),
                findViewById<ImageView>(R.id.iconTabScanner),
                findViewById<TextView>(R.id.textTabScanner)
            ),
            Triple(
                findViewById<LinearLayout>(R.id.tabProfile),
                findViewById<ImageView>(R.id.iconTabProfile),
                findViewById<TextView>(R.id.textTabProfile)
            )
        )

        val activeColor = ContextCompat.getColor(this, R.color.text_info)
        val inactiveColor = ContextCompat.getColor(this, R.color.text_tertiary)

        tabs.forEachIndexed { index, (container, icon, label) ->
            if (index == activeIndex) {
                container.setBackgroundResource(R.drawable.bg_tab_active)
                icon.setColorFilter(activeColor)
                label.setTextColor(activeColor)
                label.setTypeface(null, Typeface.BOLD)
            } else {
                container.background = null
                icon.setColorFilter(inactiveColor)
                label.setTextColor(inactiveColor)
                label.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val TAG_CATALOG = "catalog"
        private const val TAG_WARDROBE = "wardrobe"
        private const val TAG_SCANNER = "scanner"
        private const val TAG_PROFILE = "profile"
    }
}
