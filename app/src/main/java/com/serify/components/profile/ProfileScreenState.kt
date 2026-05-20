package com.serify.components.profile

data class ProfileScreenState(
    val userName: String = "Juan Pérez",
    val email: String = "usuario@gmail.com",
    val photoUrl: String? = null,
    val savedCount: Int = 0,
    val watchedCount: Int = 47,
    val ratingsCount: Int = 23
)