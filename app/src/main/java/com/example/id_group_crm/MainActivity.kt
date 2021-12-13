package com.example.id_group_crm

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.id_group_crm.Activitys.Create_User
import com.example.id_group_crm.Activitys.Maps
import com.example.id_group_crm.Activitys.UserActivity
import com.example.id_group_crm.Fragments.Chat
import com.example.id_group_crm.Fragments.Home
import com.example.id_group_crm.Fragments.Map
import com.example.id_group_crm.Fragments.Tasks
import com.example.id_group_crm.Model.UserMap
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    var draweopen=true
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val REQUEST_CHECK_SETTINGS = 10001;

    //for image

    lateinit var mImageUri: Uri
    lateinit var mStorageRef: StorageReference
    lateinit var mDatabaseRef: DatabaseReference
    lateinit var profileimage:CircleImageView
    var lat:Double=0.0
    var lon:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        buildLocationRequest()
        turnGPS_ON()

        val view=navigationview.getHeaderView(0)
        val textviewfromheader=view.findViewById<TextView>(R.id.textviewheaderusername)
        profileimage=view.findViewById<CircleImageView>(R.id.profile_image)
        supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Home()).commit()
        val map=Map()
        val home=Home()

        val databaseReference2= FirebaseDatabase.getInstance().getReference().child("Users").child(Constants.useruid)
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef=databaseReference2
        databaseReference2.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                Map.lat = snapshot.child("lat").getValue() as Double
//                Map.lon = snapshot.child("lon").getValue() as Double
                textviewfromheader.text=snapshot.child("username").getValue() as String
                Constants.username=snapshot.child("username").getValue() as String
                //Constants.userimage=snapshot.child("userimage").getValue() as String


                lastlocation()
//                Toast.makeText(this@MainActivity, Constants.userimage, Toast.LENGTH_SHORT).show()
//                Picasso.get().load(Constants.userimage).placeholder(R.drawable.idgroup).fit().centerCrop().into(profileimage)
//                // onMapReady(googleMap)

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

        GlobalScope.launch(Dispatchers.Main) {
            while (true){
                delay(60000)
                    lastlocation()

            }

        }


        //Toast.makeText(this@MainActivity,Constants.useruid,Toast.LENGTH_LONG).show()


//        imageviewadduser.setOnClickListener {
//            if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){
//                startActivity(Intent(this@MainActivity,Create_User::class.java))
//            }else{//
//                Toast.makeText(this@MainActivity, "Это невозможно для тебя", Toast.LENGTH_SHORT).show()
//            }
//
//
//        }

        profileimage.setOnClickListener {
           // openFileChooser()
           // Toast.makeText(this@MainActivity,"Change profile image",Toast.LENGTH_LONG).show()

        }
        textviewfromheader.setOnClickListener {
            //Toast.makeText(this@MainActivity,"textview username",Toast.LENGTH_LONG).show()
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
                    if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){
                        startActivity(Intent(this@MainActivity,UserActivity::class.java))

                    }else{//
                        Toast.makeText(this@MainActivity, "Это невозможно для тебя", Toast.LENGTH_SHORT).show()
                    }
                    return@setNavigationItemSelectedListener true
                }

                R.id.title3->{
                    finish()

                    return@setNavigationItemSelectedListener true
                }
                else -> true
            }
        }

        bottomnavigationview.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1->{
                    supportFragmentManager.beginTransaction().replace(R.id.linearlay1,home).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item2->{
                    supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Tasks()).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item3->{
                    if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){
                        supportFragmentManager.beginTransaction().replace(R.id.linearlay1,Map()).commit()
                    }else{//
                        Toast.makeText(this@MainActivity, "Это невозможно для тебя", Toast.LENGTH_SHORT).show()
                    }




//                    finish()
//                    startActivity(Intent(this@MainActivity,Maps::class.java))


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



    fun lastlocation() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {

                //Toast.makeText(this@MainActivity,"${it.latitude} / ${it.longitude}",Toast.LENGTH_LONG).show()
                val databaseReference2=FirebaseDatabase.getInstance().getReference().child("Users").child(Constants.useruid)
                val userMap= UserMap(Constants.username,Constants.useruid,it.latitude,it.longitude)
                lat=it.latitude
                lon=it.longitude
                databaseReference2.setValue(userMap)


            }
        }

    }
    fun buildLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
    }
    fun turnGPS_ON() {


        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(applicationContext)
                .checkLocationSettings(builder.build())
        result.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse> {

            override fun onComplete(p0: Task<LocationSettingsResponse>) {
                try {
                    val response: LocationSettingsResponse = p0.getResult(ApiException::class.java)
                    //Toast.makeText(this@MainActivity, "GPS is already tured on", Toast.LENGTH_SHORT)
                        //.show()
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this@MainActivity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                           // Toast.makeText(this@MainActivity, "GPS is already tured 1", Toast.LENGTH_SHORT)

                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                        }
                    }
                }

            }

        })


    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data!!
            Picasso.get().load(mImageUri).fit().centerCrop().into(profileimage)

            //uploadimage()
        }
    }
    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    fun uploadimage(){
        if (mImageUri!=null){
           val filereference=mStorageRef.child(System.currentTimeMillis().toString()+"."+getFileExtension(mImageUri))
            filereference.putFile(mImageUri).addOnSuccessListener {
                filereference.downloadUrl.addOnSuccessListener() {

                }
            }
        }



    }

    override fun onBackPressed() {

    }






}