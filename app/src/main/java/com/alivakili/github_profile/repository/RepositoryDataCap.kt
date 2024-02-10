package com.alivakili.github_profile.repository

class RepositoryDataCap : ArrayList<RepositoryDataCap.RepositoryItem>(){
        data class RepositoryItem(
            val description: String? = "",
            val language: String? = "",
            val name: String? = "",
            val login: String? = "",
            val url: String? = "",
            val updatedAt: String? = "",
        )
}