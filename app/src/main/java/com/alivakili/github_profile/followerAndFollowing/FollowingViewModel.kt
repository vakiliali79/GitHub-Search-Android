package com.alivakili.github_profile.followerAndFollowing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alivakili.github_profile.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header

class FollowingViewModel (private val searchWord: String) : ViewModel() {

    private var _state = MutableStateFlow<FollowingState>(FollowingState.Loading)
    var state: StateFlow<FollowingState> = _state

    init {
        retrieveFollowing()
    }

    private fun retrieveFollowing() {
        val call = RetrofitClient.ApiClient.apiService.getFollowing(Header("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"),searchWord)
        if (call != null) {
            call.enqueue(object : Callback<FollowingDTO> {
                override fun onResponse(call: Call<FollowingDTO>, response: Response<FollowingDTO>) {
                    if (response.isSuccessful)
                    _state.value=FollowingState.Success(response.body())
                    else
                        _state.value=FollowingState.Failure
                }
                override fun onFailure(call: Call<FollowingDTO>, t: Throwable) {
                    _state.value=FollowingState.Failure
                }
            })
        }

    }

    companion object {
        fun factory(searchWord: String?): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return searchWord?.let { FollowingViewModel(it) } as T
                }
            }
        }
    }


}