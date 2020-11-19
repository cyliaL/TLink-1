package com.sirius.net.tlink.model

data class OffreTaxi(var uidOffer:String,
                     var idUser: String,
                    var adrDeparture:String,
                    var adrDestination:String,
                    var departLongitude: Float,var departLatitude: Float,
                     var destinationLongitude: Float,var destinationLatitude: Float,
                    var freePlaces: Int,var price:Int,val departTime:String,
                    val departDate: String,val offreState:String)