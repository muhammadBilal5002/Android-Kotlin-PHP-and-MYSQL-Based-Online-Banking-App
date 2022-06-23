package com.example.hbms

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_splash_hbms.*

class Splash_HBMS : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_hbms)
        val sharedPreferences = getSharedPreferences("save", Context.MODE_PRIVATE)
        val ip = sharedPreferences.getString("Ip","")?:""

        if(ip!=""){
        Constrain.baseline = "http://$ip/HBMS/"
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent);
        finish();
        }

    }

    override fun onDestroy() {
       // handler.removeCallbacks(Run)
        super.onDestroy()
    }

    fun add_ip(view: View) {
        var ip  = myip.text.toString().trim()
        val sharedPreferences = getSharedPreferences("save", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("Ip", ip)
            apply()
        }
        Constrain.baseline = "http://$ip/HBMS/"
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent);
        finish();
    }
}