package com.example.id_group_crm.Adapters

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.id_group_crm.Constants
import com.example.id_group_crm.Model.TaskModel
import com.example.id_group_crm.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task.*
import java.util.*

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val databaseReference= FirebaseDatabase.getInstance().getReference().child("Tasks")
        val intent=intent
        var username=intent.getStringExtra("key")
       // Toast.makeText(this@TaskActivity,username,Toast.LENGTH_SHORT).show()
        textviewTaskActivity1.setText("Вы можете поставить задачу на\n$username")
        val calendar=Calendar.getInstance()
        var year=calendar.get(Calendar.YEAR)
        var month=calendar.get(Calendar.MONTH)
        var dayofmonth=calendar.get(Calendar.DAY_OF_MONTH)
        edittextTaskActivity2.setText("$dayofmonth/${month+1}/$year")
        edittextTaskActivity2.setOnClickListener {
            val calendar=Calendar.getInstance()
            var year=calendar.get(Calendar.YEAR)
            var month=calendar.get(Calendar.MONTH)
            var dayofmonth=calendar.get(Calendar.DAY_OF_MONTH)
            var hour=calendar.get(Calendar.HOUR_OF_DAY)
            var minute=calendar.get(Calendar.MINUTE)


            val dateSetListener:DatePickerDialog.OnDateSetListener=DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                //Toast.makeText(this@TaskActivity,"$year ${month+1} $dayOfMonth",Toast.LENGTH_SHORT).show()
                edittextTaskActivity2.setText("$dayOfMonth/${month+1}/$year")
            }

            val datePickerDialog=DatePickerDialog(this@TaskActivity,android.R.style.ThemeOverlay_Material_Dialog,dateSetListener,year,month,dayofmonth)
            //datePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //Theme_Holo_Light_Dialog_MinWidth
            datePickerDialog.show()
        }
        edittextTaskActivity3.setOnClickListener {
            val calendar=Calendar.getInstance()
            var year=calendar.get(Calendar.YEAR)
            var month=calendar.get(Calendar.MONTH)
            var dayofmonth=calendar.get(Calendar.DAY_OF_MONTH)
            var hour=calendar.get(Calendar.HOUR_OF_DAY)
            var minute=calendar.get(Calendar.MINUTE)


            val dateSetListener:DatePickerDialog.OnDateSetListener=DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                //Toast.makeText(this@TaskActivity,"$year ${month+1} $dayOfMonth",Toast.LENGTH_SHORT).show()
                edittextTaskActivity3.setText("$dayOfMonth/${month+1}/$year")
            }

            val datePickerDialog=DatePickerDialog(this@TaskActivity,android.R.style.ThemeOverlay_Material_Dialog,dateSetListener,year,month,dayofmonth)
            //datePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //Theme_Holo_Light_Dialog_MinWidth
            datePickerDialog.show()
        }

        buttonTaskActivity1.setOnClickListener {
            if (edittextTaskActivity1.text.isEmpty() || edittextTaskActivity3.text.isEmpty()){
                edittextTaskActivity1.setError("Написать задание")
            }else{

                val calendar=Calendar.getInstance()
                var year=calendar.get(Calendar.YEAR)
                var month=calendar.get(Calendar.MONTH)
                var dayofmonth=calendar.get(Calendar.DAY_OF_MONTH)
                var hour=calendar.get(Calendar.HOUR_OF_DAY)
                var minute=calendar.get(Calendar.MINUTE)

                var uploadkey:String=databaseReference.push().key.toString()
                val taskmodel=TaskModel(username!!,edittextTaskActivity1.text.toString(),"s0",edittextTaskActivity2.text.toString(),edittextTaskActivity3.text.toString(),uploadkey)
                databaseReference.child(uploadkey).setValue(taskmodel)
                edittextTaskActivity1.setText("")
                edittextTaskActivity3.setText("")
                Toast.makeText(this@TaskActivity,"Задача добавлена",Toast.LENGTH_SHORT).show()



            }
        }

    }
}