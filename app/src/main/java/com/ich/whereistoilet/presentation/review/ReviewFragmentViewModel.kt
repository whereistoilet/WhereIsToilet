package com.ich.whereistoilet.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ich.whereistoilet.common.DBKey
import com.ich.whereistoilet.common.util.Resource
import com.ich.whereistoilet.domain.model.Review
import com.ich.whereistoilet.domain.model.ToiletInfo
import com.ich.whereistoilet.domain.repository.ToiletInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewFragmentViewModel @Inject constructor(
    private val repository: ToiletInfoRepository
): ViewModel() {
    private val _state = MutableLiveData(ReviewPageState())
    val state: LiveData<ReviewPageState> = _state

    private val _event = MutableLiveData<UiEvent>()
    val event: LiveData<UiEvent> = _event

    val auth by lazy {Firebase.auth}
    private val reviewPerUserDB by lazy { Firebase.database.reference.child(DBKey.DB_REVIEW_USER).child(auth.currentUser!!.uid) }
    private val reviewPerToiletDB by lazy { Firebase.database.reference.child(DBKey.DB_REVIEW_TOILET) }

    private var toiletDB: DatabaseReference? = null

    private val readReviewListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val list = snapshot.children.map{ it.getValue(Review::class.java) }
            _state.value = _state.value?.copy(
                reviewList = list.filterNotNull(),
                isLoading = false
            )
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    init {
        getToiletInfo()
    }

    fun getToiletInfo() = viewModelScope.launch{
        _state.value = _state.value?.copy(isLoading = true)

        val result = repository.getToiletInfo()
        when(result){
            is Resource.Success -> {
                _state.value = _state.value?.copy(
                    toilets = result.data!!,
                    isLoading = false,
                    errorMsg = ""
                )
            }
            is Resource.Error -> {
                _state.value = _state.value?.copy(
                    errorMsg = result.message ?: "",
                    isLoading = false
                )
            }
        }
    }

    fun selectToilet(toilet: ToiletInfo){
        _state.value = _state.value?.copy(selectedToilet = toilet)
        toiletDB = reviewPerToiletDB.child(toilet.id)
        toiletDB!!.addListenerForSingleValueEvent(readReviewListener)
    }

    fun createReview(review: Review) {
        reviewPerUserDB.push().setValue(review)

        toiletDB?.child(review.getReviewString())?.setValue(review)?.addOnCompleteListener {
            if(it.isSuccessful){
                val list = _state.value!!.reviewList.toMutableList()
                list.add(review)
                _state.value = _state.value?.copy(
                    reviewList = list,
                    isLoading = false
                )
                _event.value = UiEvent.CloseDialog
                sortOptionChanged(SortOption.Recommendation)
            }
        }
    }

    fun sortOptionChanged(option: SortOption){
        val reviews = _state.value!!.reviewList
        when(option){
            is SortOption.Recent -> {
                _state.value = _state.value?.copy(
                    reviewList = reviews.sortedWith{r1, r2 -> (r1.date - r2.date).toInt()}
                )
            }
            is SortOption.Recommendation -> {
                _state.value = _state.value?.copy(
                    reviewList = reviews.sortedWith{r1, r2 -> r1.thumbUp - r2.thumbUp}
                )
            }
        }
    }

    fun thumbUpClicked(idx: Int, review: Review){
        val uid = auth.currentUser?.uid

        if(uid == null){
            _event.value = UiEvent.ShowToast("로그인이 필요합니다")
            return
        }

        val ref = toiletDB?.child(review.getReviewString())
        ref?.runTransaction(object: Transaction.Handler{
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                var rev = currentData.getValue(Review::class.java) ?: return Transaction.success(currentData)

                val map = rev.thumbUpUsers.toMutableMap()
                if(rev.thumbUpUsers.containsKey(uid)){
                    map.remove(uid)
                    rev = rev.copy(
                        thumbUp = rev.thumbUp - 1,
                        thumbUpUsers = map
                    )
                }else{
                    map[uid] = true
                    rev = rev.copy(
                        thumbUp = rev.thumbUp + 1,
                        thumbUpUsers = map
                    )
                }

                val reviewList = _state.value!!.reviewList.toMutableList()
                reviewList[idx] = rev

                _state.postValue(_state.value?.copy(
                    reviewList = reviewList
                ))

                currentData.value = rev
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {

            }
        })
    }

    sealed class SortOption{
        object Recent: SortOption()
        object Recommendation: SortOption()
    }

    sealed class UiEvent{
        data class ShowToast(val msg: String): UiEvent()
        object CloseDialog: UiEvent()
    }
}