package com.sirius.net.tlink.ui.covoiturageMyOffers

import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.Place
import com.sirius.net.tlink.model.OffreCovoiturage
import com.sirius.net.tlink.model.OrderCovoiturage

class CovoiturageMyOffersViewModel : ViewModel() {
    var myOfferCov : OffreCovoiturage = OffreCovoiturage()

    fun setMyOrdersCovoiturage(offerCov: OffreCovoiturage){
        myOfferCov=offerCov
    }


    fun setOrderDate(day: Int, month: Int, year: Int) {
        myOfferCov.departDate = "$year-$month-$day"
    }

    fun setOrderTime(hour: Int, minute: Int) {
        myOfferCov.departTime = "$hour:$minute"
    }

    fun setDepartPlace(place: Place) {
        myOfferCov.adrDeparture = place.address!!
        myOfferCov.departLatitude = place.latLng?.latitude?.toFloat()!!
        myOfferCov.departLongitude = place.latLng?.longitude?.toFloat()!!
    }

    fun setDestinationPlace(place: Place) {
        myOfferCov.adrDestination = place.address!!
        myOfferCov.destinationLatitude = place.latLng?.latitude?.toFloat()!!
        myOfferCov.destinationLongitude = place.latLng?.longitude?.toFloat()!!
    }

    fun setPlacesnum(placeNum: String) {
        myOfferCov.freePlaces = placeNum.toInt()
    }

    fun setOrderNote(note: String) {
        myOfferCov.note = note
    }

    fun setIdOrder(id: String) {
        myOfferCov.uidOffer = id
    }

}