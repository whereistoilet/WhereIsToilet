package com.ich.whereistoilet.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ich.whereistoilet.R
import com.ich.whereistoilet.databinding.FragmentReviewBinding

class ReviewFragment: Fragment(R.layout.fragment_review) {
    private lateinit var binding: FragmentReviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReviewBinding.bind(view)
    }
}