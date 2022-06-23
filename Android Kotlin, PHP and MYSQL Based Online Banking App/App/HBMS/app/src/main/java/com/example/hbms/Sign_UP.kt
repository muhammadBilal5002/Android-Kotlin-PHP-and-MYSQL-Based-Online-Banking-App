package com.example.hbms

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hbms.Request.IRRequest
import com.example.hbms.Request.Register_User_Request
import com.example.hbms.Response.Register_User_Response
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Sign_UP : AppCompatActivity(), Callback<Register_User_Response> {
    var LD = LoadingDialog(this)
    private var myemail = ""
    private var myphone = ""
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    fun Submit(view: View) {
        var name = Signup_Name.text.toString().trim()
        var email = Signup_Email.text.toString().trim()
        var password = Signup_Password.text.toString().trim()
        var phoneNo = Signup_PhoneNo.text.toString().trim()
        var flag = validate(name,email,password,phoneNo)

        if(flag){
            var req = Register_User_Request(userName=name,userEmail=email,userPassword=password,userPhoneno=phoneNo)
            var res = request_contract.myrequest(req)
            myemail = email
            myphone = password
            res.enqueue(this)
            LD.startLoading()
        }

        else{
            Toast.makeText(this,"Please Fill Correct Information",Toast.LENGTH_SHORT).show()
        }
    }



    fun validate(name:String,email: String,password:String,Phone:String):Boolean {
        val emailRegex = Regex("^[\\w!#\$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$")
        val phone = Regex("^((\\+92)?(0092)?(92)?(0)?)(3)([0-9]{9})\$")
        if (!name.isNotEmpty() || !(password.length>5) || !emailRegex.matches(email) || !phone.matches(Phone) || email.contains("@HBMS.FM.PK",true)){
            return false
        }
        return true
    }

    override fun onResponse(call: Call<Register_User_Response>, response: Response<Register_User_Response>) {
        LD.isDismiss()
        var resp = response.body()
        if(resp!=null){
            if(resp.status){
                AlertDialog.Builder(this).apply {
                    setTitle("Added Successfully")
                    setMessage("Email: $myemail \n\nPassword: $myphone")
                    setPositiveButton("Ok") { dialogInterface, i ->
                        var intent = Intent(this@Sign_UP,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.create().show()
            }
            else{
                Toast.makeText(this,"Email Address Already Exist",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFailure(call: Call<Register_User_Response>, t: Throwable) {
        LD.isDismiss()
        Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show()
    }

}