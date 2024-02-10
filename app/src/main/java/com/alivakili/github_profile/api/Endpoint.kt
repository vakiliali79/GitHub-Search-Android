package com.alivakili.github_profile.api

import android.provider.ContactsContract.CommonDataKinds.Organization
import com.alivakili.github_profile.followerAndFollowing.FollowersDTO
import com.alivakili.github_profile.followerAndFollowing.FollowingDTO
import com.alivakili.github_profile.languageDTO.Language
import com.alivakili.github_profile.repository.Repository
import com.alivakili.github_profile.userDTO.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface Endpoint {


    @GET("users/{searchWord}")
    fun getUser(

        @Header("Authorization")header: Header,
        @Path("searchWord") type: String

    ): Call<User>


    @GET("search/repositories")
    fun getLanguage(
        @Query("q")programmingLanguage:String?,
        @Header("Authorization")header: Header
        //must be like language:xxxxx
    ):Call<Language>


    @GET("users/")
    fun getOrganization(
        @Header("Authorization")header: Header
    ):Call<Organization>

    @GET("users/{searchWord}/following")
    fun getFollowing(
        @Header("Authorization")header: Header,
        @Path("searchWord") type: String
    ):Call<FollowingDTO>
    @GET("users/{searchWord}/followers")
    fun getFollowers(
        @Header("Authorization")header: Header,
        @Path("searchWord") type: String
    ):Call<FollowersDTO>

    @GET("users/{searchWord}/repos")
    fun getRepository(
        @Header("Authorization")header: Header,
        @Path("searchWord") type: String?
    ):Call<Repository>

}
