package com.example.khansolo.model

/**
 * This class handles  Custom Exception.
 */
class CustomException: Exception{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}