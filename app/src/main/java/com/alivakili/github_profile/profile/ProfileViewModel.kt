package com.alivakili.github_profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alivakili.github_profile.api.RetrofitClient
import com.alivakili.github_profile.userDTO.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header

class ProfileViewModel(private val searchWord: String) :
    ViewModel() {
    private var _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    var state: StateFlow<ProfileState> = _state

    init {
            retrieveUserData()
    }

    private fun retrieveUserData() {
        val call = RetrofitClient.ApiClient.apiService.getUser(
            Header("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"),
            searchWord
        )
        if (call != null) {
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        _state.value = ProfileState.SuccessOnUser(profileDOT = body)
                    } else {
                        _state.value = ProfileState.Failure
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _state.value = ProfileState.Failure
                }
            })
        }

    }




    companion object {
        fun factory(searchWord: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProfileViewModel(searchWord = searchWord) as T
                }
            }
        }
    }

}