package com.sirius.net.tlink.adapters

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.model.DateTime
import java.util.*

class OrdersAdapter(val navController: NavController)
    :RecyclerView.Adapter<OrdersAdapter.OrderHolder>()
        , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var currentDateTime = DateTime()
    var savedDateTime = DateTime()
    lateinit var context: Context

    override fun getItemCount(): Int = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
       val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return OrderHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.orderContainer.setOnClickListener {
            when(position){
                0->{ startDialogFlow()}
                1->{ navController.navigate(R.id.nav_to_marchandise) }
                2->{ navController.navigate(R.id.nav_to_medical) }
                3->{ navController.navigate(R.id.nav_to_covoiturage) }
            }
        }
        when(position){
            0->{
                holder.orderName.text = holder.itemView.context.getString(R.string.chauffeur)
                holder.orderImage.setImageResource(R.drawable.ic_taxi)
            }
            1->{
                holder.orderName.text = holder.itemView.context.getString(R.string.marchandise)
                holder.orderImage.setImageResource(R.drawable.ic_shipping)
            }
            2->{
                holder.orderName.text = holder.itemView.context.getString(R.string.transportmedical)
                holder.orderImage.setImageResource(R.drawable.ic_medical_transport)
            }
            3->{
                holder.orderName.text = holder.itemView.context.getString(R.string.covoiturage)
                holder.orderImage.setImageResource(R.drawable.ic_taxi)
            }
        }
    }

    private fun startDialogFlow() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.date_choice_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT
                , ConstraintLayout.LayoutParams.MATCH_PARENT)

        val todayButton:Button = dialog.findViewById(R.id.today_button)
        val dateButton:Button = dialog.findViewById(R.id.date_button)

        todayButton.setOnClickListener {
            navController.navigate(R.id.nav_to_taxi)
            dialog.dismiss()
        }

        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            currentDateTime.day = cal.get(Calendar.DAY_OF_MONTH)
            currentDateTime.month = cal.get(Calendar.MONTH)
            currentDateTime.year = cal.get(Calendar.YEAR)
            currentDateTime.hour = cal.get(Calendar.HOUR)
            currentDateTime.minute = cal.get(Calendar.MINUTE)
            DatePickerDialog(context,this,currentDateTime.year,currentDateTime.month,currentDateTime.day).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //TODO post the info to the backend
        savedDateTime.day = dayOfMonth
        savedDateTime.month = month
        savedDateTime.year = year

        TimePickerDialog(context,this,currentDateTime.hour,currentDateTime.minute,true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //TODO post the info to the backend and start the new fragment
        savedDateTime.hour = hourOfDay
        savedDateTime.minute = minute
        navController.navigate(R.id.nav_to_taxi)
    }

    class OrderHolder(inflater:LayoutInflater,parent:ViewGroup)
        :RecyclerView.ViewHolder(inflater.inflate(R.layout.order_item,parent,false)) {
        val orderName:TextView = itemView.findViewById(R.id.order_name)
        val orderImage:ImageView = itemView.findViewById(R.id.order_preview)
        val orderContainer:ConstraintLayout = itemView.findViewById(R.id.main_container)
    }


}