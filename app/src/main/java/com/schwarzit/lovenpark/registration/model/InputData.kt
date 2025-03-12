package com.schwarzit.lovenpark.registration.model

interface InputData {

    fun isDataValid(): Boolean

    fun isDataEmpty(): Boolean
}