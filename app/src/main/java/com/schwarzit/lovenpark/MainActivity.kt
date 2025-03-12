package com.schwarzit.lovenpark

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.databinding.ActivityMainBinding
import com.schwarzit.lovenpark.mappin.ui.viewmodel.MapPinsViewModel
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import com.schwarzit.lovenpark.utils.Util.Companion.ANDROID_WEB_KIT
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val checkConnection by lazy { this.let { NetworkLiveData(it) } }
    private val mapPinsViewModel: MapPinsViewModel by viewModels ()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContentView(binding.root)

        val mToolbar = binding.topAppBar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        //Enable navigation via back arrow on phone
        navController.enableOnBackPressed(true)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.googleMapFragment,
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        actionBarAppearance()
        setupNavBarButtons()
        observeNavigationChanges()

        checkConnection.observe(this) { isConnected ->
            if (isConnected) {
                mapPinsViewModel.getUserWhenOnline()
            } else {
                mapPinsViewModel.requestMapPinsWhenOffline()
                SocialUserSharedPrefs.removeUser(this)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (
            view != null
            && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE)
            && view is EditText
            && !view.javaClass.name.startsWith(ANDROID_WEB_KIT)
        ) {
            val touchArea = IntArray(2)
            view.getLocationOnScreen(touchArea)
            val x = ev.rawX + view.getLeft() - touchArea[0]
            val y = ev.rawY + view.getTop() - touchArea[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken, 0
                )
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else {
            navController.navigateUp() || super.onSupportNavigateUp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun actionBarAppearance() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination.id == R.id.socialLoginFragment
                || destination.id == R.id.loginRegistrationFragment
                || destination.id == R.id.googleMapFragment
                || destination.id == R.id.locationsFragment
                || destination.id == R.id.onboardingFragment
            ) {
                binding.layoutAppBar.visibility = View.GONE

            } else {
                binding.layoutAppBar.visibility = View.VISIBLE

            }
        }
    }

    private fun removeItemsUnderline(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.menu.forEach { item ->
            val content = SpannableString(item.title.toString())
            content.removeSpan(UnderlineSpan())
            item.title = content
        }
    }

    private fun underlineMenuItem(item: MenuItem) {
        val content = SpannableString(item.title)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        item.title = content
    }

    private fun checkFlagStatusLogin() {
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            navController.navigate(R.id.loginRegistrationFragment)
        } else {
            navController.navigate(R.id.socialLoginFragment)
        }
    }

    private fun isUserLogged() =
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            UserSharedPrefHelper.getLoggedUserEmail(applicationContext)?.isNotEmpty() == true
        } else {
            SocialUserSharedPrefs.getLoggedUser(applicationContext)?.isNotEmpty() == true
        }

    private fun setupNavBarButtons() {
        binding.menuButtonMap.setOnClickListener {
            navController.navigate(R.id.googleMapFragment)
        }

        binding.menuButtonSites.setOnClickListener {
            navController.navigate(R.id.locationsFragment)
        }

        binding.menuButtonSignal.setOnClickListener {
            if(isUserLogged()){
                navController.navigate(R.id.signalRootFragment)
            } else {
                navController.navigate(R.id.socialLoginFragment)
            }
        }

        binding.menuButtonAbout.setOnClickListener {
            navController.navigate(R.id.aboutSectionFragment)
        }

        binding.menuButtonProfile.setOnClickListener {
            navController.navigate(R.id.userAccountFragment)
        }
    }

    private fun updateNavBarSelection(selectedItem: Button) {
        binding.bottomBarContainer.children.filter { it is Button }
            .forEach {
                it.isSelected = false
            }
        selectedItem.isSelected = true
    }

    private fun observeNavigationChanges() {
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            supportActionBar?.setHomeAsUpIndicator(R.drawable.top_bar_back_arrow)
            binding.apply {
                val isUserLogged = isUserLogged()
                when {
                    navDestination.id == R.id.aboutSectionFragment -> {
                        binding.bottomBarContainer.visibility = View.VISIBLE
                        updateNavBarSelection(binding.menuButtonAbout)
                    }

                    navDestination.id == R.id.googleMapFragment -> {
                        binding.bottomBarContainer.visibility = View.VISIBLE
                        updateNavBarSelection(binding.menuButtonMap)
                    }

                    navDestination.id == R.id.signalRootFragment -> {
                        binding.bottomBarContainer.visibility = View.GONE
                        updateNavBarSelection(binding.menuButtonSignal)
                    }

                    navDestination.id == R.id.userAccountFragment && isUserLogged -> {
                        binding.bottomBarContainer.visibility = View.GONE
                        updateNavBarSelection(binding.menuButtonProfile)
                    }

                    navDestination.id == R.id.userAccountFragment && !isUserLogged -> {
                        binding.bottomBarContainer.visibility = View.GONE
                        updateNavBarSelection(binding.menuButtonProfile)
                        navController.navigateUp()
                        checkFlagStatusLogin()
                    }

                    navDestination.id == R.id.locationsFragment -> {
                        binding.bottomBarContainer.visibility = View.VISIBLE
                        updateNavBarSelection(binding.menuButtonSites)
                    }

                    else -> {
                        binding.bottomBarContainer.visibility = View.GONE
                    }
                }
            }
        }
    }
}