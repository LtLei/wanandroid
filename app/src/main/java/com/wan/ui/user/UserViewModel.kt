package com.wan.ui.user

import com.wan.core.base.BaseViewModel
import com.wan.data.user.UserRepository

class UserViewModel(userRepository: UserRepository) : BaseViewModel() {
    val user = userRepository.getUserLiveData()
}