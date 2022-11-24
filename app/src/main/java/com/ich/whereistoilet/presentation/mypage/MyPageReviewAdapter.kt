package com.ich.whereistoilet.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ich.whereistoilet.R
import com.ich.whereistoilet.common.util.DateUtil
import com.ich.whereistoilet.databinding.ReviewItemBinding
import com.ich.whereistoilet.domain.model.Review

class MyPageReviewAdapter
    : ListAdapter<Review, MyPageReviewAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ReviewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(reviewItem: Review){
            binding.userIdTextView.text = reviewItem.userId
            binding.contentTextView.text = reviewItem.content
            binding.reviewRatingBar.rating = reviewItem.stars
            binding.dateTextView.text = DateUtil.timeMillisToDate(reviewItem.date)
            binding.recommendationButton.isGone = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Review>(){
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }
        }
    }
}