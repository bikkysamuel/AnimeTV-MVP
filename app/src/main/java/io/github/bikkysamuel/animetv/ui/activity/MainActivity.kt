package io.github.bikkysamuel.animetv.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import io.github.bikkysamuel.animetv.R
import io.github.bikkysamuel.animetv.listeners.callbacks.VideoPlayerFragmentCallbackListener
import io.github.bikkysamuel.animetv.listeners.fragments.LoadDetailPageFragmentListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.ui.fragments.BrowseFragment
import io.github.bikkysamuel.animetv.ui.fragments.DashboardFragment
import io.github.bikkysamuel.animetv.ui.fragments.SettingsFragment
import io.github.bikkysamuel.animetv.ui.fragments.VideoPlayerFragment

class MainActivity : AppCompatActivity(), OnItemSelectedListener,
    LoadDetailPageFragmentListener, VideoPlayerFragmentCallbackListener {

    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var browseFragment: BrowseFragment
    private lateinit var settingsFragment: SettingsFragment

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Hide Action Bar
        supportActionBar?.hide()

        initializeFragments()
        initializeNavView()
        loadFragment(dashboardFragment, true)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.nav_dashboard -> {
                loadFragment(dashboardFragment, true)
            }
            R.id.nav_browse -> {
                loadFragment(browseFragment, true)
            }
            R.id.nav_settings -> {
                loadFragment(settingsFragment, true)
            }
            else -> false
        }
    }

    private fun initializeFragments() {
        dashboardFragment = DashboardFragment(this)
        browseFragment = BrowseFragment(this)
        settingsFragment = SettingsFragment()
    }

    private fun initializeNavView() {
        bottomNavView = findViewById(R.id.bottomNavBar)
        bottomNavView.setOnItemSelectedListener(this@MainActivity)
    }

    private fun loadFragment(fragment: Fragment, isItNavViewFragment: Boolean): Boolean {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (!isItNavViewFragment)
            fragmentTransaction.addToBackStack(null)
        fragmentTransaction
            .replace(R.id.mainFragment, fragment)
            .commit()
        return isItNavViewFragment
    }

    override fun loadVideoPlayerFragment(browseAnimeItem: BrowseAnimeItem) {
        val videoPlayerFragment = VideoPlayerFragment
            .newInstance(browseAnimeItem = browseAnimeItem, videoPlayerFragmentCallbackListener = this)
        loadFragment(videoPlayerFragment, false)
        bottomNavView.visibility = View.GONE
    }

    override fun goBackFromVideoPlayerPage() {
        bottomNavView.visibility = View.VISIBLE
    }

    override fun updateFavouritesInDashboard() {
        dashboardFragment.updateFavouriteList()
    }
}
