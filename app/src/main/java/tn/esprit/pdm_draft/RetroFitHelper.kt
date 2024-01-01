package tn.esprit.pdm_draft

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {


    fun getInstance(): Retrofit {
        Log.e("gggg","vvv");
        return Retrofit.Builder().baseUrl("http://10.0.2.2:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()

    }
}