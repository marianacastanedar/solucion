package com.gt.uvg.rickandmorty.data.model

sealed interface AuthStatus {
    object Loading : AuthStatus
    object LoggedIn : AuthStatus
    object LoggedOut : AuthStatus
    object Error : AuthStatus
}