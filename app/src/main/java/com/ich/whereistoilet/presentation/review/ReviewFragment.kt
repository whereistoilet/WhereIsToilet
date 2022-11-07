package com.ich.whereistoilet.presentation.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.ich.whereistoilet.R
import com.ich.whereistoilet.databinding.FragmentReviewBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class ReviewFragment: Fragment(R.layout.fragment_review), OnMapReadyCallback{
    private lateinit var binding: FragmentReviewBinding

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.reviewMapFrameLayout) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.reviewMapFrameLayout, it).commit()
            }
        mapFragment?.getMapAsync(this)

        val scrollView = binding.reviewScrollView
        val frameLayout = binding.reviewMapFrameLayout as TouchFrameLayout
        frameLayout.setTouchListener(object: TouchFrameLayout.OnTouchListener{
            override fun onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true)
            }
        })

        return binding.root
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.620160688284, 127.05484288329))
        naverMap.moveCamera(cameraUpdate)

        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false

        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
    }

    companion object{
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}