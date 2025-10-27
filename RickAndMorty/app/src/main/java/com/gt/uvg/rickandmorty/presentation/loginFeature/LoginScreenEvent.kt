package com.gt.uvg.rickandmorty.presentation.loginFeature

sealed interface LoginScreenEvent {
    data class UpdateName(val name: String) : LoginScreenEvent
    data object LogInClick : LoginScreenEvent
}
