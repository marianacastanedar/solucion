package com.gt.uvg.rickandmorty.presentation.loginFeature

data class LoginScreenState(
    val name: String = "",
    val syncing: Boolean = false,
    val finishedSync: Boolean = false
)
