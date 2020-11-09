package com.sirius.net.tlink.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R

class OrdersAdapter(val navController: NavController):RecyclerView.Adapter<OrdersAdapter.OrderHolder>() {


    override fun getItemCount(): Int = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
       val inflater = LayoutInflater.from(parent.context)
        return OrderHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.orderContainer.setOnClickListener {
            when(position){
                0->{ navController.navigate(R.id.nav_to_taxi) }
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

    class OrderHolder(inflater:LayoutInflater,parent:ViewGroup)
        :RecyclerView.ViewHolder(inflater.inflate(R.layout.order_item,parent,false)) {
        val orderName:TextView = itemView.findViewById(R.id.order_name)
        val orderImage:ImageView = itemView.findViewById(R.id.order_preview)
        val orderContainer:ConstraintLayout = itemView.findViewById(R.id.main_container)
    }
}