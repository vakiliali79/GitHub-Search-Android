package com.alivakili.github_profile.repository

sealed class RepositoryState {
    object Loading: RepositoryState()
    data class Success(val repository: RepositoryDataCap?): RepositoryState()
    data class SuccessOnLanguage(val repository: RepositoryDataCap?):RepositoryState()
    object Failure: RepositoryState()
}