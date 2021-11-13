package com.example.id_group_crm

import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.id_group_crm.Activitys.Maps
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyBackgroundTask: BroadcastReceiver() {
    companion object{
        var ACTION_PROCESS_START="startlocation"
        var ACTION_PROCESS_STOP="stoplocation"
    }



    fun ggwp(){
        GlobalScope.launch(Dispatchers.Main) {
            while (true){

                delay(2000)
                Log.d("Pr","negap")

            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        ggwp()
    }


}