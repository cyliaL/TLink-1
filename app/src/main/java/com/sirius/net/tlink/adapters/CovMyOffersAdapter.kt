package com.sirius.net.tlink.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.model.Historique
import com.sirius.net.tlink.model.OffreCovoiturage

class CovMyOffersAdapter(myOffersList: ArrayList<OffreCovoiturage>)
    : RecyclerView.Adapter<CovMyOffersAdapter.CovMyOffersHolder>() {

    private var adapterMyOffersList: ArrayList<OffreCovoiturage> = ArrayList()

    init {
        if (myOffersList != null)
            adapterMyOffersList = myOffersList
    }

    override fun getItemCount(): Int = adapterMyOffersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovMyOffersHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CovMyOffersHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CovMyOffersHolder, position: Int) {
        val item = adapterMyOffersList[position]

        holder.date_my_offer.text=item.departDate.toString()
        holder.time_my_Offer.text=item.departTime.toString()
        holder.departure_my_offer.text=item.adrDeparture
        holder.destination_my_offer.text=item.adrDestination
        holder.price.text=item.price.toString()

    }

    class CovMyOffersHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cov_my_offers_item, parent, false)) {
        val fullname: TextView = itemView.findViewById(R.id.fullName_myOffer)
        var date_my_offer: TextView = itemView.findViewById(R.id.date_ref_select_myOffer)
        val time_my_Offer: TextView = itemView.findViewById(R.id.time_ref_select_myOffer)
        val departure_my_offer: TextView = itemView.findViewById(R.id.depart_myOffer)
        val destination_my_offer: TextView = itemView.findViewById(R.id.arrivee_myOffer)
        val price:TextView = itemView.findViewById(R.id.prix_myOffer)

    }
}
