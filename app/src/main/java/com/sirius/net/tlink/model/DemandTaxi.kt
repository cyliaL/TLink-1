package com.sirius.net.tlink.model

data class DemandTaxi(var idUser: String = "",var idDemand:String ="",
                 var adrDeparture:String ="",
                 var adrDestination:String="",
                 var departLongitude: Float =0F,var departLatitude: Float=0F,
                 var destinationLongitude: Float=0F,var destinationLatitude: Float=0F,
                 var nbrPassengers: Int=0,var departTime:String="",var Note:String="",
                 var departDate: String="",var demandState:String="",var isAlert: Int=0)
