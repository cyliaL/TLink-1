package com.sirius.net.tlink.model

data class OrderCovoiturage
    (var uidDemand:String="",
     var idUser: String="",
     var adrDeparture:String="",
     var adrDestination:String="",
     var departLongitude: Float=0F, var departLatitude: Float=0F,
     var destinationLongitude: Float=0F, var destinationLatitude: Float=0F,
     var nbrPassengers: Int=0, var price:Int=0, var departTime:String="",
     var departDate: String="", val demandState:String="", var isAlert: Int=0, var note:String="",var user:User=User())