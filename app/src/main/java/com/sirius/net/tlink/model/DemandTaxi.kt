package com.sirius.net.tlink.model

data class DemandTaxi(var idUser: String,
                 var adrDeparture:String,
                 var adrDestination:String,
                 var departLongitude: Float,var departLatitude: Float,
                 var destinationLongitude: Float,var destinationLatitude: Float,
                 var nbrPassengers: Int,val departTime:String,
                 val departDate: String,val demandState:String,var isAlert: Int)
