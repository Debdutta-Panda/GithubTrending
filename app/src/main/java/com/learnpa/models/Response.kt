package com.learnpa.models

import java.lang.Exception

data class Response<T>(
    val data: T?,
    val exception: Exception?
)
