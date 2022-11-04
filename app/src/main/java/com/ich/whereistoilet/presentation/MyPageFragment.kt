package com.ich.whereistoilet.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ich.whereistoilet.R
import com.ich.whereistoilet.databinding.FragmentMypageBinding

class MyPageFragment: Fragment(R.layout.fragment_mypage) {
    private lateinit var binding: FragmentMypageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMypageBinding.bind(view)
    }
}