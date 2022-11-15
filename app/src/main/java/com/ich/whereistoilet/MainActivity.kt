package com.ich.whereistoilet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ich.whereistoilet.presentation.review.ReviewFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment: MapFragment = MapFragment.newInstance()
        mapFragment.getMapAsync(this)

        val navHostController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostController.navController

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setupWithNavController(navController)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.620160688284, 127.05484288329))
        naverMap.moveCamera(cameraUpdate)

        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true

        locationSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode != LOCATION_PERMISSION_REQUEST_CODE)
            return

        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)){
            if(!locationSource.isActivated){
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}