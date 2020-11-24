package com.sirius.net.tlink.model

data class OffreTaxi(var uidOffer:String ="",
                     var idPartner: String="",
                    var adrDeparture:String="",
                    var adrDestination:String="",
                    var departLongitude: Double=0.0,var departLatitude: Double=0.0,
                     var destinationLongitude: Double=0.0,var destinationLatitude: Double=0.0,
                    var freePlaces: Int=0,var price:Int=0,var departTime:String="",
                    var departDate: String="",var offreState:String="")