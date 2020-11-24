package com.sirius.net.tlink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.model.OffreTaxi

interface TaxiOffersClick {
    fun onClick(position: Int)
}

class TaxiOffersAdapter(offersList: ArrayList<OffreTaxi>?,var clickListener:TaxiOffersClick)
    :RecyclerView.Adapter<TaxiOffersAdapter.TaxiOffersHolder>(){

    private var adapterOffersList: ArrayList<OffreTaxi> = ArrayList()

    init {
        if(offersList != null)
            adapterOffersList = offersList
    }

    override fun getItemCount(): Int = adapterOffersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxiOffersHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaxiOffersHolder(inflater,parent,clickListener)
    }

    override fun onBindViewHolder(holder: TaxiOffersHolder, position: Int) {
        val item = adapterOffersList[position]

        holder.price.text = "${item.price} DZA"
        holder.depart.text = item.adrDeparture
        holder.destination.text = item.adrDestination
        holder.freePlaces.text = "Places vides: ${item.freePlaces}"
        holder.time.text = item.departTime
    }



    class TaxiOffersHolder(inflater: LayoutInflater,parent: ViewGroup,var clickListener:TaxiOffersClick)
        :RecyclerView.ViewHolder(inflater.inflate(R.layout.offer_item,parent,false)),
    View.OnClickListener{

        val price:TextView = itemView.findViewById(R.id.prix_offre)
        val depart:TextView = itemView.findViewById(R.id.offer_departure)
        val destination:TextView = itemView.findViewById(R.id.offer_destination)
        val freePlaces:TextView = itemView.findViewById(R.id.offer_places)
        val time:TextView = itemView.findViewById(R.id.offer_time)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onClick(adapterPosition)
        }
    }
}
