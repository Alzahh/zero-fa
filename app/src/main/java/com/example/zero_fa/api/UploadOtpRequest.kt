package com.example.zero_fa.api

import com.example.zero_fa.modules.Totp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT

interface UploadOtpRequest {
    @Headers("Content-Type: application/json")
    @PUT(".")
    fun uploadOtp(@Body body: ArrayList<Totp>) : Call<UploadOtpResponse>
}