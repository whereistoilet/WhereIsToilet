package com.ich.whereistoilet.presentation.mypage

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ich.whereistoilet.R
import com.ich.whereistoilet.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment: Fragment(R.layout.fragment_mypage) {
    private val auth by lazy { Firebase.auth }
    private lateinit var binding: FragmentMypageBinding

    private val viewModel by viewModels<MyPageViewModel>()

    private val adapter = MyPageReviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMypageBinding.bind(view)
        initialize()
        initViewModel()
    }

    private fun initialize(){
        if(auth.currentUser != null)
            changeMenuIfLogin()
        else
            changeMenuIfNotLogin()

        binding.mypageIdTextView.setOnClickListener {
            if(auth.currentUser == null){
                LoginDialogFragment(loginSuccess = {
                    changeMenuIfLogin()
                }).show(childFragmentManager, "LoginDialog")
            }
        }

        binding.mypageLogoutSignUpButton.setOnClickListener {
            if(binding.mypageLogoutSignUpButton.text.toString() == resources.getString(R.string.logout)){
                auth.signOut()
                changeMenuIfNotLogin()
            }else{
                SignUpDialogFragment().show(childFragmentManager, "SignupDialog")
            }
        }

        binding.myPageReviewRecyclerView.adapter = adapter
        viewModel.getUserReviews()
    }

    private fun initViewModel(){
        viewModel.review.observe(requireActivity()){
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun changeMenuIfLogin(){
        binding.mypageIdTextView.text = auth.currentUser?.email ?: ""
        binding.mypageIdTextView.setTextColor(Color.BLACK)
        binding.mypageLogoutSignUpButton.text = "로그아웃"
        binding.myPageReviewRecyclerView.isGone = false
        binding.manageReviewTextView.isGone = false
    }

    private fun changeMenuIfNotLogin(){
        binding.mypageIdTextView.text = resources.getText(R.string.mention_not_login)
        binding.mypageIdTextView.setTextColor(Color.GRAY)
        binding.mypageLogoutSignUpButton.text = "회원가입"
        binding.myPageReviewRecyclerView.isGone = true
        binding.manageReviewTextView.isGone = true
    }
}