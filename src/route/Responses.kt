package com.wictorlyan.route

data class ResponseSuccess(
    val success: Boolean = true,
    val result: Any?
) {
    constructor(result: Any?) : this(true, result)
}

data class ResponseError(
    val success: Boolean = false,
    val errorMessage: String
) {
    constructor(errorMessage: String) : this(false, errorMessage)
}