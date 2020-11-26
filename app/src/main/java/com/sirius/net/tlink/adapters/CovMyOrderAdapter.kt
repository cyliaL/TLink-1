package com.sirius.net.tlink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.model.Historique
import com.sirius.net.tlink.model.OffreCovoiturage
import com.sirius.net.tlink.model.OrderCovoiturage


class CovMyOrderAdapter (myOrdersList: ArrayList<Historique>?)
    : RecyclerView.Adapter<CovMyOrderAdapter.CovMyOrderHolder>() {

    private var adapterMyOrdersList: ArrayList<Historique> = ArrayList()

    init {
        if (myOrdersList != null)
            adapterMyOrdersList = myOrdersList
    }

    override fun getItemCount(): Int = 6 //adapterMyOrdersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovMyOrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CovMyOrderHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CovMyOrderHolder, position: Int) {
        //val item = adapterOrdersList[position]

        //holder.name_order.text = item.price.toString()
        //holder.date_order.text = item.departDate.toString()
    }

    class CovMyOrderHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cov_my_orders_item, parent, false)) {
        val fullname: TextView = itemView.findViewById(R.id.fullName_myOrder)
        val date_my_order: TextView = itemView.findViewById(R.id.date_ref_select_myOrder)
        val time_my_Order: TextView = itemView.findViewById(R.id.time_ref_select_myOrder)
        val departure_my_order: TextView = itemView.findViewById(R.id.depart_myOrder)
        val destination_my_order: TextView = itemView.findViewById(R.id.arrivee_myOrder)

    }
}




