package com.example.id_group_crm

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.id_group_crm.Activitys.Create_User
import com.example.id_group_crm.Fragments.Chat
import com.example.id_group_crm.Fragments.Home
import com.example.id_group_crm.Fragments.Map
import com.example.id_group_crm.Fragments.Tasks
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    var draweopen=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val view=navigationview.getHeaderView(0)
        val textviewfromheader=view.findViewById<TextView>(R.id.textviewheaderusername)
        val profileimage=view.findViewById<CircleImageView>(R.id.profile_image)
        supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Home()).commit()

        imageviewadduser.setOnClickListener {
            startActivity(Intent(this@MainActivity,Create_User::class.java))

        }

        profileimage.setOnClickListener {
            Toast.makeText(this@MainActivity,"Change profile image",Toast.LENGTH_LONG).show()

        }
        textviewfromheader.setOnClickListener {
            Toast.makeText(this@MainActivity,"textview username",Toast.LENGTH_LONG).show()
        }

        drawerlayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                imageviewdehaze.setImageResource(R.drawable.ic_baseline_dehaze_24)
                linearlay1.visibility=View.VISIBLE

                draweopen=true
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                imageviewdehaze.setImageResource(R.drawable.ic_round_keyboard_backspace_24)

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
            }

            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
            }
        })

        imageviewdehaze.setOnClickListener {
            if (draweopen){
                drawerlayout.openDrawer(Gravity.LEFT)
                linearlay1.visibility=View.INVISIBLE
                draweopen=false
            }else{
                drawerlayout.closeDrawer(Gravity.LEFT)
                draweopen=true
            }

        }

        navigationview.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.title1->{
                    //drawerlayout.closeDrawer(Gravity.LEFT)
                    return@setNavigationItemSelectedListener true
                }
                R.id.title2->{
                    return@setNavigationItemSelectedListener true
                }
                R.id.title3->{
                    return@setNavigationItemSelectedListener true
                }
                else -> true
            }
        }

        bottomnavigationview.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1->{
                    supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Home()).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item2->{
                    supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Tasks()).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item3->{
                    supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Map()).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item4->{
                    supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Chat()).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                else -> true
            }
        }

    }




}