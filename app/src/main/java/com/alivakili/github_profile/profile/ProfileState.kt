package com.alivakili.github_profile.profile

import com.alivakili.github_profile.userDTO.User

sealed class ProfileState {
    object Loading: ProfileState()
    data class SuccessOnUser(val profileDOT: User?): ProfileState()
    object Failure: ProfileState()
}