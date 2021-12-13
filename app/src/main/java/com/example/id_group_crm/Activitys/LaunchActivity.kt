package com.example.id_group_crm.Activitys

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.id_group_crm.MainActivity
import com.example.id_group_crm.R

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        Handler().postDelayed({
            startActivity(Intent(this@LaunchActivity, Login_Activity::class.java))
            finish()
        },3000)
    }
}