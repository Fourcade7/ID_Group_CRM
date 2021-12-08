package com.example.id_group_crm.Activitys

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.id_group_crm.Constants
import com.example.id_group_crm.MainActivity
import com.example.id_group_crm.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val firebaseAuth= FirebaseAuth.getInstance()

        buttonlogin.setOnClickListener {
            val view= LayoutInflater.from(this@Login_Activity).inflate(R.layout.dialogregister,null)
            val dialog= Dialog(this@Login_Activity)
            val lottieAnimationView=view.findViewById<LottieAnimationView>(R.id.lottieanimationview1)

            dialog.setContentView(view)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            dialog.setCanceledOnTouchOutside(false)
            dialog.create()
            dialog.show()
            firebaseAuth.signInWithEmailAndPassword(edittextuseaccountlogin.text.toString(),edittextuserpasswordlogin.text.toString()).addOnCompleteListener{


                if (it.isSuccessful){
                    startActivity(Intent(this@Login_Activity,MainActivity::class.java))
                    Constants.useruid=firebaseAuth.currentUser!!.uid
                    dialog.dismiss()
                    finish()
                }else{
                    Toast.makeText(this@Login_Activity,"Ошибка", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }


            }
        }
    }
}