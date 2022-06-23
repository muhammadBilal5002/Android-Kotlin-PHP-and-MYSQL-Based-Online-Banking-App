package com.example.hbms

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient{
    companion object{
        private  var network: Retrofit? =null
        fun getnetworkobject(): Retrofit {
            var temp = network
            return if(temp!=null){
                temp
            }
            else{
                var instant = Retrofit.Builder()
                    .baseUrl(Constrain.baseline)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                network = instant
                instant
            }
        }
    }
}