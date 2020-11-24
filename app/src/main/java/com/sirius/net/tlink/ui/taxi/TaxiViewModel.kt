package com.sirius.net.tlink.ui.taxi

import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.Place
import com.sirius.net.tlink.model.DemandTaxi
import com.sirius.net.tlink.model.OffreTaxi

class TaxiViewModel : ViewModel() {

    var currentTaxiDemand: DemandTaxi = DemandTaxi()
    var currentTaxiOffer: OffreTaxi = OffreTaxi()

    fun setTaxiOffer(taxiOffer:OffreTaxi){
        currentTaxiOffer = taxiOffer
    }

    fun setDemandDate(day: Int, month: Int, year: Int) {
        currentTaxiDemand.departDate = "$year-$month-$day"
    }

    fun setDemandTime(hour: Int, minute: Int) {
        currentTaxiDemand.departTime = "$hour:$minute"
    }

    fun setDepartPlace(place: Place) {
        currentTaxiDemand.adrDeparture = place.address!!
        currentTaxiDemand.departLatitude = place.latLng?.latitude?.toFloat()!!
        currentTaxiDemand.departLongitude = place.latLng?.longitude?.toFloat()!!
    }

    fun setDestinationPlace(place: Place) {
        currentTaxiDemand.adrDestination = place.address!!
        currentTaxiDemand.destinationLatitude = place.latLng?.latitude?.toFloat()!!
        currentTaxiDemand.destinationLongitude = place.latLng?.longitude?.toFloat()!!
    }

    fun setPlacesnum(placeNum: String) {
        currentTaxiDemand.nbrPassengers = placeNum.toInt()
    }

    fun setDemandNote(note: String) {
        currentTaxiDemand.Note = note
    }

    fun setIdDemand(id: String) {
        currentTaxiDemand.idDemand = id
    }

}