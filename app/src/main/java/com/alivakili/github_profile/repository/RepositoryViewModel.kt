package com.alivakili.github_profile.repository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alivakili.github_profile.api.RetrofitClient
import com.alivakili.github_profile.languageDTO.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header


class RepositoryViewModel(private val searchWord: String?,private val stateWord: String) : ViewModel() {

    private var _state = MutableStateFlow<RepositoryState>(RepositoryState.Loading)
    var state: StateFlow<RepositoryState> = _state
    init {
        if (stateWord == "language")
            retrieveLanguageData()
        else
            retrieveRepository()
    }

    private fun retrieveRepository() {

        val call = RetrofitClient.ApiClient.apiService.getRepository(
            Header(
                "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"),searchWord)


        if (call != null) {
            call.enqueue(object : Callback<Repository> {
                override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        var languages: RepositoryDataCap= RepositoryDataCap()
                        for (repo in body!!){
                            languages.add(RepositoryDataCap.RepositoryItem(
                                repo.description, repo.language,
                                repo.name, repo.owner?.login,repo.url, repo.updatedAt
                            ))
                        }
                        _state.value= RepositoryState.Success(languages)

                    }
                    else
                        _state.value= RepositoryState.Failure
                }
                override fun onFailure(call: Call<Repository>, t: Throwable) {
                    Log.e("TAG", "onFailure: "+t.localizedMessage, )
                    _state.value= RepositoryState.Failure
                }
            })
        }

    }


    private fun retrieveLanguageData() {
        val call = RetrofitClient.ApiClient.apiService.getLanguage(
            programmingLanguage = searchWord,
            Header("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")
        )
        if (call != null) {
            call.enqueue(object : Callback<Language> {
                override fun onResponse(call: Call<Language>, response: Response<Language>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        var languages = RepositoryDataCap()
                        for (language in body?.items!!){
                            languages.add( RepositoryDataCap.RepositoryItem(
                                language?.description, language?.language,
                            language?.name,language?.owner?.login,language?.url,language?.updatedAt))
                        }

                        _state.value = RepositoryState.SuccessOnLanguage(languages)

                    } else {

                        _state.value = RepositoryState.Failure
                    }
                }

                override fun onFailure(call: Call<Language>, t: Throwable) {

                    _state.value = RepositoryState.Failure
                }
            })
        }

    }

    companion object {
        fun factory(searchWord: String?,stateWord: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RepositoryViewModel(searchWord,stateWord) as T
                }
            }
        }
    }


}