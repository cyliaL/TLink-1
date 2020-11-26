package com.sirius.net.tlink.adapters

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.model.DateTime
import java.util.*

class CovoiturageAdapter(val navController: NavController)
    : RecyclerView.Adapter<CovoiturageAdapter.CovoiturageHolder>()
    , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var currentDateTime = DateTime()
    var savedDateTime = DateTime()
    lateinit var context: Context
    private var position:Int?=null

    override fun getItemCount(): Int = 2

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CovoiturageAdapter.CovoiturageHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return CovoiturageAdapter.CovoiturageHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CovoiturageAdapter.CovoiturageHolder, position: Int) {
        holder.CovoiturageContainer.setOnClickListener {



            this.position=position
            when (position) {
                0 -> {
                    sameCar()

                }
                1 -> {
                    startDialogFlow(position)
                }
            }
        }
        when (position) {
            0 -> {
                holder.covoiturageName.text =
                    holder.itemView.context.getString(R.string.OffrirTrajet)
                holder.itemDescription.text=
                    holder.itemView.context.getString(R.string.OffrirTrajetDescription)
                holder.covImage.setImageResource(R.drawable.add)

            }
            1 -> {
                holder.covoiturageName.text =
                    holder.itemView.context.getString(R.string.CommanderTrajet)
                holder.itemDescription.text=
                    holder.itemView.context.getString(R.string.CommanderTrajetDesction)
                holder.covImage.setImageResource(R.drawable.search)

            }


        }

    }


    private fun startDialogFlow(position: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.date_choice_dialog_cov)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        val todayButton: Button = dialog.findViewById(R.id.today_button)
        val dateButton: Button = dialog.findViewById(R.id.date_button)
        when (position) {
            0 -> {
                todayButton.setOnClickListener {
                    navController.navigate(R.id.nav_to_covOffer)//to chage
                    dialog.dismiss()
                }
            }
            1 -> {
                todayButton.setOnClickListener {
                    navController.navigate(R.id.nav_to_covOrder)//to chage
                    dialog.dismiss()
                }
            }
        }

        val backButtonSimple = dialog.findViewById<ImageView>(R.id.back_day_simple)
        //val note = dialog.findViewById<TextView>(R.id.note_demand)

        backButtonSimple.setOnClickListener {
            /////
            dialog.dismiss()
        }
        dialog.show()


        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            currentDateTime.day = cal.get(Calendar.DAY_OF_MONTH)
            currentDateTime.month = cal.get(Calendar.MONTH)
            currentDateTime.year = cal.get(Calendar.YEAR)
            currentDateTime.hour = cal.get(Calendar.HOUR)
            currentDateTime.minute = cal.get(Calendar.MINUTE)
            DatePickerDialog(
                context,
                this,
                currentDateTime.year,
                currentDateTime.month,
                currentDateTime.day
            ).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //TODO post the info to the backend
        savedDateTime.day = dayOfMonth
        savedDateTime.month = month
        savedDateTime.year = year

        TimePickerDialog(context, this, currentDateTime.hour, currentDateTime.minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //TODO post the info to the backend and start the new fragment
        savedDateTime.hour = hourOfDay
        savedDateTime.minute = minute
        when (position) {
            0 -> {
                navController.navigate(R.id.nav_to_covOffer)
            }//a changer
            1 -> {
                navController.navigate(R.id.nav_to_covOrder)
            }

        }
    }
    private fun sameCar() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.same_car)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT
            , ConstraintLayout.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(false)

        val yesSameCarButton = dialog.findViewById<Button>(R.id.yes_same_car_button)
        yesSameCarButton.setOnClickListener {
            navController.navigate(R.id.nav_to_covOffer)
            dialog.dismiss()
        }
        val noSameCarButton = dialog.findViewById<Button>(R.id.no_same_car_button)
        noSameCarButton.setOnClickListener {
            infoCar()
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun infoCar() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.info_car)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT
            , ConstraintLayout.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(false)

        val yesInfoCarButton = dialog.findViewById<Button>(R.id.yes_info_car_button)
        yesInfoCarButton.setOnClickListener {
            navController.navigate(R.id.nav_to_covOffer)
            dialog.dismiss()
        }
        dialog.show()
    }

    class CovoiturageHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.covoiturage_item, parent, false)) {
        val covoiturageName: TextView = itemView.findViewById(R.id.covoiturage_name)
        val CovoiturageContainer: ConstraintLayout = itemView.findViewById(R.id.mainCov_container)
        val itemDescription: TextView=itemView.findViewById(R.id.item_description)
        val covImage:ImageView = itemView.findViewById(R.id.imagecovitem)


        init{
            itemView.setOnClickListener{
                val position:Int=adapterPosition

            }
        }
    }
}



