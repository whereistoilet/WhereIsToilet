package com.ich.whereistoilet.presentation.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ich.whereistoilet.R
import com.ich.whereistoilet.databinding.FragmentCreateReviewBinding

class CreateReviewFragment: Fragment(R.layout.fragment_create_review) {
    private lateinit var binding: FragmentCreateReviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCreateReviewBinding.bind(view)
    }
}