package com.example.zero_fa.modules

import android.util.Log
import com.example.zero_fa.api.UploadOtpRequest
import com.example.zero_fa.api.UploadOtpResponse
import com.example.no_fa.dao.PasswordDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Network(private val dao: PasswordDao) {

    //    %USERPROFILE%\AppData\Local\Android\sdk\platform-tools
    private val base_url = "http://localhost:2104"


    fun uploadPasswords(totps: ArrayList<Totp>) {

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(base_url)
            .build()
            .create(UploadOtpRequest::class.java)

        val retrofitData = retrofitBuilder.uploadOtp(totps)

        retrofitData.enqueue(object : Callback<UploadOtpResponse> {
            override fun onResponse(
                call: Call<UploadOtpResponse>,
                response: Response<UploadOtpResponse>
            ) {
                val response_body = response.body()!!
                Log.d("my_logs", response_body.toString())
            }

            override fun onFailure(call: Call<UploadOtpResponse>, t: Throwable) {
                Log.d("my_logs", "Error : ${t.message}")
            }
        })
    }

}