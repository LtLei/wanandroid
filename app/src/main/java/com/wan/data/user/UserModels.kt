package com.wan.data.user

import androidx.room.Entity
import androidx.room.TypeConverters
import com.wan.db.IntListTypeConverters
import kotlinx.serialization.Serializable

@Serializable
@Entity(primaryKeys = ["id"])
@TypeConverters(IntListTypeConverters::class)
data class User(
    val id: Int,
    val nickname: String? = null,
    val icon: String? = null,
    val email: String? = null,
    val token: String? = null,
    val collectIds: List<Int>? = null
)

data class LoginFormState(
    val userNameErr: Int? = null,
    val passwordErr: Int? = null,
    val valid: Boolean = false
)