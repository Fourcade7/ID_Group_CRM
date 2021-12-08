package com.example.id_group_crm.Activitys

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.view.*


class Maps : AppCompatActivity(), OnMapReadyCallback {
    //for permission
    lateinit var locationRequest: LocationRequest
    val REQUEST_CHECK_SETTINGS = 10001;

    //for current location
    lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        var lat: Double = 0.0
        var lon: Double = 0.0
    }



    //update lccation
    lateinit var locationCallback: LocationCallback

    //for map
    lateinit var googleMap: GoogleMap
    //for read from databaes
    val arrayList = ArrayList<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        window.statusBarColor = Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //start
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //update location
        buildLocationRequest()
        turnGPS_ON()
        lastlocation()
        startLocationUpdates()

//        var mapFragment =
//            supportFragmentManager.findFragmentById(R.id.googlemap) as SupportMapFragment
//        mapFragment.getMapAsync(this@Maps)

        readfromdatabase()


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
                    Toast.makeText(this@Maps, "GPS is already tured on", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this@Maps,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                            Toast.makeText(this@Maps, "GPS is already tured 1", Toast.LENGTH_SHORT)

                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                        }
                    }
                }

            }

        })


    }

    fun lastlocation() {
        if (ActivityCompat.checkSelfPermission(
                this@Maps,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@Maps,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this@Maps,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude
                lon = it.longitude
                Toast.makeText(this@Maps, "${it.latitude}  ${it.longitude}", Toast.LENGTH_SHORT)
                    .show()
               // textview3.text = "$lat / $lon"
                //onMapReady(googleMap)

                // Log.d("Pr","${it.latitude}  ${it.longitude}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                RESULT_OK -> {
                    Toast.makeText(this, "GPS is tured on", Toast.LENGTH_SHORT).show()

                    Toast.makeText(this, "GPS required to be tured on", Toast.LENGTH_SHORT).show()
//                    GlobalScope.launch(Dispatchers.Main) {
//
//                        delay(3000)
//                        lastlocation()
//                    }

                }
                RESULT_CANCELED -> Toast.makeText(
                    this,
                    "GPS required to be tured on",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun buildLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
    }

    fun startLocationUpdates() {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Log.d("Pr", "${location.latitude} / ${location.longitude}")
                    lat = location.latitude
                    lon = location.longitude
                    onMapReady(googleMap)
                    //textview3.text = "${location.latitude} / ${location.longitude}"
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    fun startjob() {
        GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                delay(2000)
                Log.d("Pr", "Negap")
            }

        }
//        val intent=Intent(this@Maps,MyBackgroundTask::class.java)
//        pendingIntent=PendingIntent.getBroadcast(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
//
//        alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1*60*1000,pendingIntent)


    }

    fun createNotificationChannel() {


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Personal Notification"
            val descriptionText = "channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        //Simple notification

    }

//    override fun onMapReady(p0: GoogleMap) {
//        googleMap = p0
//        // lastlocation()
//
//        GlobalScope.launch(Dispatchers.Main) {
//            googleMap.clear()
//            delay(100)
//            val beruniy = LatLng(lat, lon)
//            val markerOptions1 = MarkerOptions()
//                .position(beruniy)
//                .title("Beruniy")
//                .snippet("axaxaxaxa")
//
//            val idgroup = LatLng(41.5512370, 60.6271150)
//            val markerOptions2 = MarkerOptions()
//                .position(idgroup)
//                .title("Id Group")
//                .snippet("axaxaxaxa")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder5))
//
//            val make = LatLng(41.6758649, 60.7396075)
//            val markerOptions3 = MarkerOptions()
//                .position(make)
//                .title("Make")
//                .snippet("axaxaxaxa")
//
//
//            googleMap.addMarker(markerOptions1)
//            googleMap.addMarker(markerOptions2)
//            googleMap.addMarker(markerOptions3)
//
//            if (camera) {
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beruniy, 12f))
//                camera = false
//
//            }
//
//
//        }
//    }


    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        // lastlocation()

        GlobalScope.launch(Dispatchers.Main) {
            googleMap.clear()
            delay(100)

            for (i in 0..arrayList.size-1){

                var idgroup = LatLng(00.00,00.00)
                var markerOptions = MarkerOptions()
                    .position(idgroup)
                    .title("Id Group")
                    .snippet("axaxaxaxa")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder5))
                googleMap.addMarker(markerOptions)
            }





//            if (camera) {
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beruniy, 12f))
//                camera = false
//
//            }
//            val sydney = LatLng(-34.0, 151.0)
//            googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//            // googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


        }
    }


    fun readfromdatabase(){
        val databaseReference= FirebaseDatabase.getInstance().getReference().child("Workers")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (datasnapshot in snapshot.children){
                    val users=datasnapshot.getValue(Users::class.java)
                    arrayList.add(users!!)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}