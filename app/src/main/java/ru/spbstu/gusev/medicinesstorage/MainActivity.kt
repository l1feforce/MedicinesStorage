package ru.spbstu.gusev.medicinesstorage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.spbstu.gusev.medicinesstorage.data.network.grls.GrlsServiceRepository


class MainActivity : AppCompatActivity() {

    val grlsServiceRepository: GrlsServiceRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setDefaultNightMode(MODE_NIGHT_NO)
        //delegate.applyDayNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_medicines, R.id.navigation_reminders, R.id.navigation_account
            )
        )
        setupToolbar()
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setupBottomNavBar()
        lifecycle.coroutineScope.launch {
            grlsServiceRepository.wakeUpServer()
        }
    }

    private fun setupBottomNavBar() {
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_medicine_details -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }
                R.id.navigation_medicines_search -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }
                R.id.navigation_scanner -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.GONE
                }
                R.id.navigation_reminder_details -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }
                R.id.navigation_adding_new_search -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupToolbar() {
        val radius =
            resources.getDimension(R.dimen.action_bar_corners_radius)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        val materialShapeDrawable = toolbar.background as MaterialShapeDrawable
        materialShapeDrawable.shapeAppearanceModel = materialShapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}