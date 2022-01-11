package com.hausberger.mvistarter.framework.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.databinding.ActivityMainBinding
import com.hausberger.mvistarter.framework.presentation.common.BottomNavController
import com.hausberger.mvistarter.framework.presentation.common.BottomNavController.*
import com.hausberger.mvistarter.framework.presentation.common.setUpNavigation
import com.hausberger.mvistarter.framework.presentation.details.DetailsFragment
import com.hausberger.mvistarter.util.Constants.BundleKeys.Companion.BOTTOM_NAV_BACK_STACK_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), OnNavigationGraphChanged, OnNavigationReselectedListener {

    private lateinit var binding: ActivityMainBinding

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.fragmentContainer,
            R.id.menu_a,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation(savedInstanceState)
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle? = null) {
        binding.bottomNavigationView.setUpNavigation(
            bottomNavController = bottomNavController,
            onReselectListener = this
        )

        if (savedInstanceState == null) {
            bottomNavController.setupBottomNavigationBackStack(null)
            bottomNavController.onNavigationItemSelected()
        } else {
            (savedInstanceState[BOTTOM_NAV_BACK_STACK_KEY] as IntArray?)?.let { items ->
                val backStack = BackStack()
                backStack.addAll(items.toTypedArray())
                bottomNavController.setupBottomNavigationBackStack(backStack)
            }
        }
    }

    override fun onBackPressed() {
        if (bottomNavController.isNavigationBackStackInitialized()) {
            bottomNavController.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // save backstack for bottom nav
        if (bottomNavController.isNavigationBackStackInitialized()) {
            outState.putIntArray(
                BOTTOM_NAV_BACK_STACK_KEY,
                bottomNavController.navigationBackStack.toIntArray()
            )
        }
    }

    override fun onGraphChange() {
        // in this sample we have to nothing to do here
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) {
        when (fragment) {

            is DetailsFragment -> {
                navController.navigate(R.id.action_detailsFragment_to_home)
            }
        }
    }
}