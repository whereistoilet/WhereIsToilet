package com.ich.whereistoilet.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.ich.whereistoilet.common.DBKey
import com.ich.whereistoilet.domain.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference
): ViewModel() {
    private val _reviews = MutableLiveData<List<Review>>()
    val review: LiveData<List<Review>> = _reviews

    fun getUserReviews() = viewModelScope.launch {
        if(auth.currentUser != null) {
            val reviewDB = dbReference.child(DBKey.DB_REVIEW_USER).child(auth.currentUser!!.uid)
            reviewDB.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userReviews = snapshot.children.map{ it.getValue<Review>()}
                    _reviews.value = userReviews.filterNotNull().sortedWith{r1, r2 ->
                        (r2.date - r1.date).toInt()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}