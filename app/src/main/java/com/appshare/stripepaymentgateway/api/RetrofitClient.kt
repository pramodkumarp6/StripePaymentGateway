package com.appshare.stripepaymentgateway.api

import com.appshare.stripepaymentgateway.utils.URLs
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {


        /* fun provideRetrofit():Retrofit{

            return Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build()

        }
        fun ApiServices(retrofit:Retrofit):ApiServices{
            return retrofit.create(ApiServices::class.java)
        }*/


        val instance: ApiServices by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(URLs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ApiServices::class.java)
        }
    }
}