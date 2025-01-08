package com.example.customview

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.layout_custom_view_1.oneMotionLayout
import kotlinx.android.synthetic.main.layout_custom_view_2.twoMotionLayout
import kotlinx.android.synthetic.main.layout_custom_view_3.threeMotionLayout
import kotlinx.android.synthetic.main.layout_custom_view_4.fourMotionLayout

class MainActivity : AppCompatActivity() {

    private var firstPressedTime = 0L
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val destinationMap = mapOf(
            R.id.oneFragment to oneMotionLayout,
            R.id.twoFragment to twoMotionLayout,
            R.id.threeFragment to threeMotionLayout,
            R.id.fourFragment to fourMotionLayout
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(destinationMap.keys).build()
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration)
        destinationMap.forEach { item ->
            item.value.setOnClickListener { navController.navigate(item.key) }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            controller.popBackStack()
            destinationMap.values.forEach { it.progress = 0f }
            destinationMap[destination.id]?.transitionToEnd()
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime <= 2000){
            super.onBackPressed()
            finish()
        }else{
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show()
            firstPressedTime = System.currentTimeMillis()
        }
    }
}