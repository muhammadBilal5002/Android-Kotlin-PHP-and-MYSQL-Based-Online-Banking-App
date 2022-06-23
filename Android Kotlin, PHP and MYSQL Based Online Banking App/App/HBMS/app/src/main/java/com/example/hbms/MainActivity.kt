package com.example.hbms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hbms.Request.IRRequest
import com.example.hbms.Request.Login_Request
import com.example.hbms.Response.Login_Response
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.jvm.JvmName as KotlinJvmJvmName


class MainActivity() : AppCompatActivity(), Callback<Login_Response>{
    var LD = LoadingDialog(this)
    lateinit var pass:String
    lateinit var action:String
    lateinit var localDataProvider:LocalDataProvider
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        localDataProvider = LocalDataProvider(this)
        var email = localDataProvider.getData("email")
        if(email!=""){
            if(email.contains("@HBMS.FM.PK",true)){
                goto_LoginFM_Dashboard()
            }
            else {
                goto_Login_Dashboard()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Login_Email.setText("")
        Login_Password.setText("")
    }

    fun Sign_Up(view: View) {
        val intent = Intent(this,Sign_UP::class.java)
        startActivity(intent);
    }

    fun Login(view: View) {
        var email = Login_Email.text.toString().trim()
        var password = Login_Password.text.toString().trim()
        var flag = validate(email,password)

        if(flag){
            pass = password
            lateinit var req:Login_Request
            if(email.contains("@HBMS.FM.PK",true)){
                action = "LOGIN_FM"
                 req = Login_Request(action="LOGIN_FM",userEmail = email, userPassword = password)
            }
            else {
                action="LOGIN_USER"
                req = Login_Request(userEmail = email, userPassword = password)
            }
                var res = request_contract.login_request(req)

                res.enqueue(this)


            LD.startLoading()
        }
        else{
            Toast.makeText(this,"Please Fill Correct Information",Toast.LENGTH_SHORT).show()
        }

    }

    fun validate(email: String,password:String):Boolean {
        val emailRegex = Regex("^[\\w!#\$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$")
        val phone = Regex("^((\\+92)?(0092)?(92)?(0)?)(3)([0-9]{9})\$")
        if (!(password.length>5) || !emailRegex.matches(email)){
            return false
        }
        return true
    }
    fun goto_Login_Dashboard(){
        var intent = Intent(this,Login_Dashboard::class.java)
        startActivity(intent)
        finish()
    }
    fun goto_LoginFM_Dashboard(){
        var intent = Intent(this,Franchise_Manager_Dashboard::class.java)
        startActivity(intent)
        finish()
    }


    override fun onResponse(call: Call<Login_Response>, response: Response<Login_Response>) {
        LD.isDismiss()
        var resp = response.body()
        if(resp!=null){
            if(resp.status){
                localDataProvider.saveData("email",resp.userEmail)
                localDataProvider.saveData("name",resp.userName)
                localDataProvider.saveData("balance",resp.userBalance?:"0")
                localDataProvider.saveData("phone",resp.userPhoneno)
                localDataProvider.saveData("password",pass)
                localDataProvider.saveData("action",action)
                if (resp.userEmail.contains("@HBMS.FM.PK",true)){
                    localDataProvider.saveData("withdraw",resp.userWithDraw)
                    goto_LoginFM_Dashboard()

                }
                else{
                    goto_Login_Dashboard()
                }
            }
            else{
                Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onFailure(call: Call<Login_Response>, t: Throwable) {
        LD.isDismiss()
        Toast.makeText(this,"${t.message}",Toast.LENGTH_SHORT).show()
    }



}