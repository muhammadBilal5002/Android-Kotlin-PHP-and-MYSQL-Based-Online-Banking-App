package com.example.hbms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hbms.Request.IRRequest
import com.example.hbms.Request.Login_Request
import com.example.hbms.Response.Login_Response
import kotlinx.android.synthetic.main.activity_franchise_manager_dashboard.*
import kotlinx.android.synthetic.main.activity_franchise_manager_dashboard.Acc_Balance
import kotlinx.android.synthetic.main.activity_franchise_manager_dashboard.Acc_Email
import kotlinx.android.synthetic.main.activity_franchise_manager_dashboard.name
import kotlinx.android.synthetic.main.activity_login_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Franchise_Manager_Dashboard : AppCompatActivity(), Callback<Login_Response> {
    lateinit  var localDataProvider:LocalDataProvider
    var LD = LoadingDialog(this)
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localDataProvider = LocalDataProvider(this)
        setContentView(R.layout.activity_franchise_manager_dashboard)
        Acc_Email.text = "Email: " + localDataProvider.getData("email" )
        name.text = localDataProvider.getData("name")
    }
    override fun onStart() {
        super.onStart()
        Acc_Balance.text = "Balance: " + localDataProvider.getData("balance")?:"0"
        Acc_WithDraw.text = "WithDraw Limit Remaining: " + localDataProvider.getData("withdraw")?:"0"
    }

    fun Add_Balance(view: View) {
        var intent = Intent(this,Add_Balance::class.java)
        startActivity(intent)

    }
    fun WithDraw_Balance(view: View) {
        var intent = Intent(this,WithDraw_Balance::class.java)
        startActivity(intent)
    }
    fun Transaction_History(view: View) {
        val intent = Intent(this,TransactionHistory::class.java)
        startActivity(intent);
    }

    fun Logout(view: View) {
        localDataProvider.clearAllData()
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }


        fun Refresh(view: View) {
            var email = localDataProvider.getData("email")
            var password = localDataProvider.getData("password")
            var action = localDataProvider.getData("action")
            var req = Login_Request(action=action,userEmail = email, userPassword = password)
            var res = request_contract.login_request(req)
            res.enqueue(this)
            LD.startLoading()

        }

    override fun onResponse(call: Call<Login_Response>, response: Response<Login_Response>) {
        LD.isDismiss()
        var resp = response.body()
        if(resp!=null){
            if(resp.status){
                localDataProvider.saveData("withdraw",resp.userWithDraw)
                Acc_WithDraw.text = "WithDraw Limit Remaining: " + resp.userWithDraw?:"0"
                localDataProvider.saveData("withdraw",resp.userBalance)
                Acc_Balance.text = "Balance: " + resp.userBalance
            }
            else{
                Toast.makeText(this,"${resp.message}", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"Server Side Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<Login_Response>, t: Throwable) {
        Toast.makeText(this,"${t.message}",Toast.LENGTH_SHORT).show()
    }

}