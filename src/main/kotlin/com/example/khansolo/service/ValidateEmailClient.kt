package com.example.khansolo.service

import com.example.khansolo.model.EmailValidation
import io.micrometer.core.annotation.Timed
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
/**
 * Spring Cloud OpenFeign client for accessing api-layer open source email validation api
 */
@FeignClient(value = "ValidateFeign", url = "http://apilayer.net")
interface ValidateEmailClient {
    @GetMapping("/api/check")
    @Timed(value = "validate.email.time", description = "Time taken by validating emails ")
    fun validateEmail(
        @RequestParam(value = "email") email : String,
        @RequestParam(value = "smtp") smtp : Int = 0,
        @RequestParam(value = "format") format : Int = 0,
        @RequestParam(value = "access_key") access_key : String = "b1dbeff9f8919516891df7cdcf25484b"
    ): EmailValidation
}