package com.example.id_group_crm.Activitys

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.id_group_crm.MyBackgroundTask
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
import com.google.android.gms.tasks.OnSuccessListener


class Maps : AppCompatActivity() {
    //for permission
    lateinit var locationRequest: LocationRequest
    val REQUEST_CHECK_SETTINGS = 10001;
    //for current location
    lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object{
        var lat:Double=0.0
        var lon:Double=0.0


    }
    //update lccation
     lateinit var locationCallback: LocationCallback




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //start
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //update location
        buildLocationRequest()
        turnGPS_ON()

        startjob()
//        lastlocation()
//        startLocationUpdates()


    }

    fun turnGPS_ON() {


        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(applicationContext).checkLocationSettings(builder.build())
        result.addOnCompleteListener(object :OnCompleteListener<LocationSettingsResponse>{

            override fun onComplete(p0: Task<LocationSettingsResponse>) {
                try {
                    val response: LocationSettingsResponse=p0.getResult(ApiException::class.java)
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

    fun lastlocation(){
        if (ActivityCompat.checkSelfPermission(
                this@Maps,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@Maps,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this@Maps, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it!=null){
                lat=it.latitude
                lon=it.longitude
                Toast.makeText(this@Maps,"${it.latitude}  ${it.longitude}", Toast.LENGTH_SHORT).show()
                textview3.text="$lat / $lon"

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
                    GlobalScope.launch(Dispatchers.Main) {

                        delay(3000)
                        lastlocation()
                    }

                }
                RESULT_CANCELED -> Toast.makeText(
                    this,
                    "GPS required to be tured on",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun buildLocationRequest(){
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
    }

    fun startLocationUpdates() {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    Log.d("Pr","${location.latitude} / ${location.longitude}")
                    textview3.text="${location.latitude} / ${location.longitude}"
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
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }



    fun startjob(){

        val intent=Intent(this@Maps,MyBackgroundTask::class.java)
       val pendingIntent=PendingIntent.getBroadcast(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val  alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1*60*1000,pendingIntent)
    }






}