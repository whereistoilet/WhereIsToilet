package com.ich.whereistoilet.presentation.review

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ich.whereistoilet.databinding.FragmentCreateReviewBinding
import com.ich.whereistoilet.domain.model.Review
import com.ich.whereistoilet.domain.model.ToiletInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateReviewFragment(
    private val selectedToilet: ToiletInfo,
    private val createReview: (Review) -> Unit
): DialogFragment() {
    private val auth by lazy { Firebase.auth }
    private lateinit var binding: FragmentCreateReviewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        binding = FragmentCreateReviewBinding.inflate(layoutInflater)
        initialize()

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun initialize(){
        binding.toiletNameTextView.text = selectedToilet.name
        binding.toiletTypeTextView.text = selectedToilet.type

        binding.createReviewButton.setOnClickListener {
            val userId = auth.currentUser?.email
            val toiletId = selectedToilet.id
            val stars = binding.starsRatingBar.rating
            val cleanRate = binding.cleanRatingBar.rating
            val content = binding.reviewEditText.text.toString()
            val date = System.currentTimeMillis()

            createReview(
                Review(
                    userId = userId ?: return@setOnClickListener,
                    toiletId = toiletId,
                    stars = stars,
                    cleanRate = cleanRate,
                    content = content,
                    date = date,
                    thumbUp = 0,
                    thumbUpUsers = emptyMap()
                )
            )
        }
    }
}