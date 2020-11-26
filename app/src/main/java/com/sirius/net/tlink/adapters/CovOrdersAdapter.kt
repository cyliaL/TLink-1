package com.sirius.net.tlink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.model.OffreCovoiturage
import com.sirius.net.tlink.model.OrderCovoiturage

interface CovOrdersClick {
    fun onClick(position: Int)
}

class CovOrdersAdapter (ordersList: ArrayList<OrderCovoiturage>?, var clickListener:CovOrdersClick)
    : RecyclerView.Adapter<CovOrdersAdapter.CovOrdersHolder>(){

    private var adapterOrdersList: ArrayList<OrderCovoiturage> = ArrayList()

    init {
        if(ordersList != null)
            adapterOrdersList = ordersList
    }

    override fun getItemCount(): Int = 6 //adapterOffersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovOrdersHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CovOrdersHolder(inflater,parent,clickListener)
    }

    override fun onBindViewHolder(holder: CovOrdersHolder, position: Int) {
        //val item = adapterOrdersList[position]

        //holder.name_order.text = item.price.toString()
        //holder.date_order.text = item.departDate.toString()
    }

    class CovOrdersHolder(inflater: LayoutInflater, parent: ViewGroup, var clickListener:CovOrdersClick)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.cov_order_item,parent,false)),
        View.OnClickListener{

        val name_ref: TextView = itemView.findViewById(R.id.name_order)
        val date_order: TextView = itemView.findViewById(R.id.date_ref_select_order)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onClick(adapterPosition)
        }
    }
}

