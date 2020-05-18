package com.wan.data.user

import androidx.room.Entity
import androidx.room.TypeConverters
import com.wan.db.IntListTypeConverters

@Entity(primaryKeys = ["id"])
@TypeConverters(IntListTypeConverters::class)
data class User(
    val id: Int,
    val nickname: String?,
    val icon: String?,
    val email: String?,
    val token: String?,
    val collectIds: List<Int>?
)

data class LoginFormState(
    val userNameErr: Int? = null,
    val passwordErr: Int? = null,
    val valid: Boolean = false
)