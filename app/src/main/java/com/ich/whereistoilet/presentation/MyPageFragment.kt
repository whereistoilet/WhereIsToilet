package com.ich.whereistoilet.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ich.whereistoilet.R
import com.ich.whereistoilet.databinding.FragmentMypageBinding

class MyPageFragment: Fragment(R.layout.fragment_mypage) {
    private val auth by lazy { Firebase.auth }
    private lateinit var binding: FragmentMypageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMypageBinding.bind(view)
        initialize()
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
    }

    private fun changeMenuIfLogin(){
        binding.mypageIdTextView.text = auth.currentUser?.email ?: ""
        binding.mypageIdTextView.setTextColor(Color.BLACK)
        binding.mypageLogoutSignUpButton.text = "로그아웃"
        binding.manageReviewCardView.isGone = false
        binding.manageReviewTextView.isGone = false
    }

    private fun changeMenuIfNotLogin(){
        binding.mypageIdTextView.text = resources.getText(R.string.mention_not_login)
        binding.mypageIdTextView.setTextColor(Color.GRAY)
        binding.mypageLogoutSignUpButton.text = "회원가입"
        binding.manageReviewCardView.isGone = true
        binding.manageReviewTextView.isGone = true
    }
}