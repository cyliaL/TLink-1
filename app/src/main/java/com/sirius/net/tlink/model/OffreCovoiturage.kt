package com.sirius.net.tlink.model

data class OffreCovoiturage(
        var uidOffer:String="",
        var idUser: String="",
        var adrDeparture:String="",
        var adrDestination:String="",
        var departLongitude: Float=0F, var departLatitude: Float=0F,
        var destinationLongitude: Float=0F, var destinationLatitude: Float=0F,
        var freePlaces: Int=0, var price:Int=0, var departTime:String="",
        var departDate: String="", val offreState:String="",var note:String="")