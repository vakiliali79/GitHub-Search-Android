package com.alivakili.github_profile.followerAndFollowing

sealed class FollowingState {
    object Loading: FollowingState()
    data class Success(val following:FollowingDTO?): FollowingState()
    object Failure: FollowingState()
}