package com.alivakili.github_profile.followerAndFollowing

sealed class FollowersState {
    object Loading: FollowersState()
    data class Success(val followers:FollowersDTO?): FollowersState()
    object Failure: FollowersState()
}