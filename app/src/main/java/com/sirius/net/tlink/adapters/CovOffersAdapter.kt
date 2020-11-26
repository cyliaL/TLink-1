package com.sirius.net.tlink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.model.OffreCovoiturage

interface CovOffersClick {
    fun onClick(position: Int)
}

class CovOffersAdapter (offersList: ArrayList<OffreCovoiturage>?,var clickListener:CovOffersClick)
    : RecyclerView.Adapter<CovOffersAdapter.CovOffersHolder>(){

    private var adapterOffersList: ArrayList<OffreCovoiturage> = ArrayList()

    init {
        if(offersList != null)
            adapterOffersList = offersList
    }

    override fun getItemCount(): Int = 6 //adapterOffersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovOffersHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CovOffersHolder(inflater,parent,clickListener)
    }

    override fun onBindViewHolder(holder: CovOffersHolder, position: Int) {
        //val item = adapterOffersList[position]

        //holder.name_offer.text = item.price.toString()
        //holder.date_offer.text = item.departDate.toString()
        //holder.prix_offer.text = item.price.toString()
    }



    class CovOffersHolder(inflater: LayoutInflater, parent: ViewGroup, var clickListener:CovOffersClick)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.cov_offer_item,parent,false)),
        View.OnClickListener{

        val name_ref: TextView = itemView.findViewById(R.id.name_offer)
        val date_offer: TextView = itemView.findViewById(R.id.date_ref_select_offer)
        val prix_offer: TextView = itemView.findViewById(R.id.prix_offer)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onClick(adapterPosition)
        }
    }
}

