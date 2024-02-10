package com.alivakili.github_profile.followerAndFollowing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alivakili.github_profile.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header

class FollowersViewModel(private val searchWord: String) : ViewModel() {

    private var _state = MutableStateFlow<FollowersState>(FollowersState.Loading)
    var state: StateFlow<FollowersState> = _state
    init {
        retrieveFollowers()
    }

    private fun retrieveFollowers() {

        val call = RetrofitClient.ApiClient.apiService.getFollowers(
            Header(
            "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"),searchWord)


        if (call != null) {
            call.enqueue(object : Callback<FollowersDTO> {
                override fun onResponse(call: Call<FollowersDTO>, response: Response<FollowersDTO>) {
                    if (response.isSuccessful)
                        _state.value=FollowersState.Success(response.body())
                    else
                        _state.value=FollowersState.Failure
                }
                override fun onFailure(call: Call<FollowersDTO>, t: Throwable) {
                    Log.e("TAG", "onFailure: "+t.localizedMessage, )
                    _state.value=FollowersState.Failure
                }
            })
        }

    }

    companion object {
        fun factory(searchWord: String?): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return searchWord?.let { FollowersViewModel(it) } as T
                }
            }
        }
    }


}