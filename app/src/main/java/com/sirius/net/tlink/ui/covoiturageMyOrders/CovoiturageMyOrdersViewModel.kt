package com.sirius.net.tlink.ui.covoiturageMyOrders

import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.Place
import com.sirius.net.tlink.model.OffreTaxi
import com.sirius.net.tlink.model.OrderCovoiturage

class CovoiturageMyOrdersViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var myOrderCov :OrderCovoiturage=OrderCovoiturage()

    fun setMyOrdersCovoiturage(orderCov:OrderCovoiturage){
        myOrderCov=orderCov
    }


    fun setOrderDate(day: Int, month: Int, year: Int) {
        myOrderCov.departDate = "$year-$month-$day"
    }

    fun setOrderTime(hour: Int, minute: Int) {
        myOrderCov.departTime = "$hour:$minute"
    }

    fun setDepartPlace(place: Place) {
        myOrderCov.adrDeparture = place.address!!
        myOrderCov.departLatitude = place.latLng?.latitude?.toFloat()!!
        myOrderCov.departLongitude = place.latLng?.longitude?.toFloat()!!
    }

    fun setDestinationPlace(place: Place) {
        myOrderCov.adrDestination = place.address!!
        myOrderCov.destinationLatitude = place.latLng?.latitude?.toFloat()!!
        myOrderCov.destinationLongitude = place.latLng?.longitude?.toFloat()!!
    }

    fun setPlacesnum(placeNum: String) {
        myOrderCov.nbrPassengers = placeNum.toInt()
    }

    fun setOrderNote(note: String) {
        myOrderCov.note = note
    }

    fun setIdOrder(id: String) {
        myOrderCov.uidDemand = id
    }
}