package com.appshare.stripepaymentgateway

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.appshare.stripepaymentgateway.api.RetrofitClient
import com.appshare.stripepaymentgateway.models.CustomerModel
import com.appshare.stripepaymentgateway.models.PaymentIntent
import com.appshare.stripepaymentgateway.utils.Config
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var paymentSheet: PaymentSheet
     var customerId: String =""
     var ephemeralKey: String =""
    var clientSecret: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        getData()
        PaymentConfiguration.init(this, Config.publishablekey)
        button = findViewById(R.id.b1)
        button.setOnClickListener {

            paymentflow()

        }
    }

    private fun paymentflow() {
        paymentSheet.presentWithPaymentIntent(
            clientSecret,

            PaymentSheet.Configuration(
                "Pramodkumar",
                PaymentSheet.CustomerConfiguration(
                    customerId,
                    ephemeralKey
                )
            )
        )
    }


    fun getData() {

        lifecycleScope.launch(Dispatchers.IO) {
            val response: Response<CustomerModel> = RetrofitClient.instance.getPaymentData()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    customerId = response.body()!!.id

                    Log.e( "getData: ",customerId )
                    getEmprahalId(customerId)
                }
            }


        }
    }

    private fun getEmprahalId(customerId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response: Response<CustomerModel> =
                RetrofitClient.instance.getEphemeralkey(customerId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {

                    ephemeralKey = response.body()!!.id
                    Log.e( "ephemeralKey: ",ephemeralKey )
                    getIntentPayment(customerId, ephemeralKey)
                }
            }


        }
    }

    private fun getIntentPayment(customerId: String, ephemeralKey: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response: Response<PaymentIntent> =
                RetrofitClient.instance.payment_intents(customerId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    clientSecret = response.body()!!.client_secret
                    Toast.makeText(this@MainActivity, "Proceed", Toast.LENGTH_LONG).show()
                }
            }


        }

    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {

        if (paymentSheetResult is PaymentSheetResult.Completed) {
            Toast.makeText(this@MainActivity, "Payment Done", Toast.LENGTH_SHORT).show()
        }

    }

}