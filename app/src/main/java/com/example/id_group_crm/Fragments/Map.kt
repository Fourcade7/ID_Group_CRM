package com.example.id_group_crm.Fragments

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.id_group_crm.Activitys.Maps
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Map : Fragment(), OnMapReadyCallback {
    lateinit var locationRequest: LocationRequest
    val REQUEST_CHECK_SETTINGS = 10001;

    //for current location
    lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        var lat: Double = 0.0
        var lon: Double = 0.0
        var username=""
    }

    var camera = true

    //update lccation
    lateinit var locationCallback: LocationCallback

    //for map
    lateinit var googleMap: GoogleMap

    //for read from databaes
    val arrayList = ArrayList<Users>()
    var idgroup = LatLng(41.5512370,60.6271150)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        readfromdatabase()
        buildLocationRequest()
        turnGPS_ON()
        lastlocation()
//        stopLocationUpdates()
//        startLocationUpdates()

        var mapFragment =
            childFragmentManager.findFragmentById(R.id.googlemap) as SupportMapFragment?

        mapFragment?.getMapAsync(this)


        return view
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
            LocationServices.getSettingsClient(context)
                .checkLocationSettings(builder.build())
        result.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse> {

            override fun onComplete(p0: Task<LocationSettingsResponse>) {
                try {
                    val response: LocationSettingsResponse = p0.getResult(ApiException::class.java)
                    //Toast.makeText(context, "GPS is already tured on", Toast.LENGTH_SHORT) .show()

                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                activity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                           // Toast.makeText(context, "GPS is already tured 1", Toast.LENGTH_SHORT)

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
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {

//                Toast.makeText(
//                    requireActivity(),
//                    "${it.latitude}  ${it.longitude}",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
                //textview3.text = "${Maps.lat} / ${Maps.lon}"
                //onMapReady(googleMap)
                //textview3.text = "${it.latitude} / ${it.longitude}"


                // Log.d("Pr","${it.latitude}  ${it.longitude}")
            }
        }
    }

    fun startLocationUpdates() {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                    for (location in locationResult.locations) {
                        Log.d("Pr", "${location.latitude} / ${location.longitude}")
                        Map.lat = location.latitude
                        Map.lon = location.longitude
                        //onMapReady(googleMap)
                        if (lat !=null && lon !=null) {
                            //textview3.text = "$lat / $lon"
                        }
                    }

            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
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

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }



    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        GlobalScope.launch(Dispatchers.Main) {
            googleMap.clear()
            delay(1000)


            for (i in 0..arrayList.size-1) {
                delay(600)
                var idgroup2 = readlatlang(i)
                var markerOptions = MarkerOptions()
                    .position(idgroup2)
                    .title(username)
                    .snippet("$i")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder5))
                googleMap.addMarker(markerOptions)
                Toast.makeText(requireActivity(), i.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        if (camera) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(idgroup, 12f))
            camera = false

        }
    }

    fun readfromdatabase() {
        val databaseReference = FirebaseDatabase.getInstance().getReference().child("Workers")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (datasnapshot in snapshot.children) {
                    val users = datasnapshot.getValue(Users::class.java)
                    arrayList.add(users!!)
                }
              // Toast.makeText(requireActivity(), arrayList.get(2).userauthuid, Toast.LENGTH_SHORT).show()

               // readlatlang(0)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun readlatlang(i:Int):LatLng{
        val databaseReference2=FirebaseDatabase.getInstance().getReference().child("Users").child(arrayList.get(i).userauthuid)


        databaseReference2.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
               lat = snapshot.child("lat").getValue() as Double
               lon = snapshot.child("lon").getValue() as Double
               username= snapshot.child("username").getValue() as String
                //Toast.makeText(requireActivity(), "Succesfuly", Toast.LENGTH_SHORT).show()
               // onMapReady(googleMap)

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        return LatLng(lat, lon)
    }





}