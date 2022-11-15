package com.ich.whereistoilet.presentation.review

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ich.whereistoilet.R
import com.ich.whereistoilet.databinding.FragmentReviewBinding
import com.ich.whereistoilet.domain.model.Review
import com.ich.whereistoilet.domain.model.ToiletInfo
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment: Fragment(R.layout.fragment_review), OnMapReadyCallback{
    private lateinit var binding: FragmentReviewBinding

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val viewModel by viewModels<ReviewFragmentViewModel>()

    private val adapter = ReviewItemAdapter{ idx, review ->
        viewModel.thumbUpClicked(idx, review)
    }

    private var dialog: CreateReviewFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.reviewMapFrameLayout) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.reviewMapFrameLayout, it).commit()
            }
        mapFragment?.getMapAsync(this)

        val scrollView = binding.reviewScrollView
        val frameLayout = binding.reviewMapFrameLayout
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
        naverMap.minZoom = 2.0

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.620160688284, 127.05484288329))
        naverMap.moveCamera(cameraUpdate)

        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true

        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        initialize()
        initViewModel()
    }

    private fun initialize(){
        binding.reviewRecyclerView.adapter = adapter
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.reviewRecyclerView.itemAnimator = null
        binding.writeReviewButton.setOnClickListener {
            if(viewModel.auth.currentUser == null){
                Toast.makeText(context, "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
            }else if(viewModel.state.value?.selectedToilet == null) {
                Toast.makeText(context, "리뷰를 확인할 화장실을 선택해주세요", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.state.value?.selectedToilet?.let { toilet ->
                    dialog = CreateReviewFragment(
                        selectedToilet = toilet,
                        createReview = {
                            viewModel.createReview(it)
                        })
                    dialog!!.show(childFragmentManager, "CreateReview")
                }
            }
        }
        binding.reviewRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        ArrayAdapter.createFromResource(requireContext(), R.array.sort_options, android.R.layout.simple_spinner_item)
            .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sortSpinner.adapter = adapter
                binding.sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        when(position){
                            0 -> viewModel.sortOptionChanged(ReviewFragmentViewModel.SortOption.Recent)
                            1 -> viewModel.sortOptionChanged(ReviewFragmentViewModel.SortOption.Recommendation)
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }
    }

    private fun initViewModel(){
        viewModel.state.observe(requireActivity()){
            if(it.errorMsg.isNotBlank())
                Toast.makeText(context, "${it.errorMsg}", Toast.LENGTH_SHORT).show()

            if(it.isLoading)
                showOrHideProgressIndicator(it.isLoading)

            if(it.toilets.isNotEmpty()){
                updateMarkers(it.toilets)
            }
            
            adapter.submitList(it.reviewList)
            adapter.notifyDataSetChanged()
            setReviewRatingsAndDivideGender(it.reviewList)

            showNoReviewText(it.reviewList.isEmpty())
        }

        viewModel.event.observe(requireActivity()){
            when(it){
                is ReviewFragmentViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
                is ReviewFragmentViewModel.UiEvent.CloseDialog -> {
                    if(dialog?.showsDialog == true)
                        dialog?.dismiss()
                }
            }
        }
    }

    private fun showOrHideProgressIndicator(show: Boolean){
        if(show)
            binding.loadingIndicator.visibility = View.VISIBLE
        else
            binding.loadingIndicator.visibility = View.GONE
    }

    private fun updateMarkers(items: List<ToiletInfo>){
        items.forEach { toilet ->
            val marker = Marker()
            marker.position = LatLng(toilet.lat, toilet.lng)
            marker.map = naverMap
            marker.tag = toilet.id
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.GREEN
            marker.onClickListener = Overlay.OnClickListener {
                selectToilet(it)
                true
            }
        }
    }

    private fun selectToilet(overlay: Overlay){
        val selectedToilet = viewModel.state.value?.toilets?.first{
            it.id == overlay.tag
        }

        viewModel.selectToilet(selectedToilet!!)

        binding.toiletNameTextView.text = selectedToilet.name
        binding.toiletTypeTextView.text = selectedToilet.type

        val cameraZoom = CameraPosition(LatLng(selectedToilet.lat, selectedToilet.lng), 16.0)
        naverMap.cameraPosition = cameraZoom

        val cameraUpdate = CameraUpdate
            .scrollTo(LatLng(selectedToilet.lat, selectedToilet.lng))
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun showNoReviewText(show: Boolean){
        binding.noReviewTextView.visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun setReviewRatingsAndDivideGender(items: List<Review>){
        var starsSum = 0f
        var cleanSum = 0f
        items.forEach {
            starsSum += it.stars
            cleanSum += it.cleanRate
        }
        binding.starsRatingBar.rating = starsSum / items.size
        binding.cleanRatingBar.rating = cleanSum / items.size
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    companion object{
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}