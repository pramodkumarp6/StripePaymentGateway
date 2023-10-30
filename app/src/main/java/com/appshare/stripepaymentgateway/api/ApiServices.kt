package com.appshare.stripepaymentgateway.api

import com.appshare.stripepaymentgateway.models.CustomerModel
import com.appshare.stripepaymentgateway.models.PaymentIntent
import com.appshare.stripepaymentgateway.utils.Config
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {


    @Headers("Authorization:Bearer ${Config.secretkey}")
     @POST("v1/customers")
     suspend fun getPaymentData():Response<CustomerModel>

    @Headers("Authorization:Bearer ${Config.secretkey}","Stripe-Version: 2023-10-16")
    @POST("v1/ephemeral_keys")
    suspend fun getEphemeralkey(
        @Query("customer")customer:String
    ):Response<CustomerModel>

    @Headers("Authorization:Bearer ${Config.secretkey}")
    @POST("v1/payment_intents")
   suspend fun payment_intents(
        @Query("customer")customerId:String,
        @Query("amount")amount:String ="100",
    @Query("currency")currency:String ="eur",
        @Query("automatic_payment_methods[enabled]") automate:Boolean = true
    ):Response<PaymentIntent>
}