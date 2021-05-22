package com.phics23.tenant.util



sealed class Result<out T> {

    class Success<out R>(val data : R) : Result<R>()
    class Failure<out R>(val message : String) : Result<R>()
    class Loading<out R>() : Result<R>()
}