package com.sirius.net.tlink.ui.covoiturageMyOffers

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sirius.net.tlink.R
import com.sirius.net.tlink.adapters.CovMyOffersAdapter

import com.sirius.net.tlink.databinding.CovoiturageMyOffersFragmentBinding
import com.sirius.net.tlink.model.OffreCovoiturage

import org.json.JSONArray
import org.json.JSONObject

class CovoiturageMyOffersFragment : Fragment() {


    private val viewModel: CovoiturageMyOffersViewModel by activityViewModels()
    private lateinit var binding: CovoiturageMyOffersFragmentBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.covoiturage_my_offers_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPrefs = requireContext().getSharedPreferences("TLINK", Context.MODE_PRIVATE)
        requestQueue = Volley.newRequestQueue(requireContext())
        showMyOrdersList()

    }


    private fun showMyOrdersList(){

        val url = "https://www.sirius-iot.eu/Dev/Tlink/Android_API_Covtrg.php?histrq_offers"
        val request = object : StringRequest(Method.POST, url,
                { response ->
                    val jsonResponse = JSONObject(response)
                    val jsonObject = jsonResponse.getJSONObject("OFFERS_HISTORIQ")
                    if (jsonObject.getString("error") == "false") {
                        // viewModel.setIdDemand(jsonObject.getString("id_order"))

                        val myOfferListRecycler = view?.findViewById<RecyclerView>(R.id.covMyOffers_recycler)
                        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

                        val adapter = CovMyOffersAdapter(mapArray(jsonObject.getJSONArray("LIST")))

                        myOfferListRecycler?.setHasFixedSize(false)
                        myOfferListRecycler?.layoutManager = layoutManager
                        myOfferListRecycler?.adapter = adapter

                    } else {
                        Toast.makeText(
                                requireContext(), jsonObject.getString("message"), Toast.LENGTH_LONG
                        ).show()
                    }

                },
                { error ->

                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    requestQueue.stop()
                }
        ){
            override fun getParams():Map<String, String> {
                val params:HashMap<String, String> = HashMap()
                //Adding parameters to request
                // val demand = viewModel.currentTaxiDemand
                params["idUser"] = "1"
                /* params["adrDeparture"] = demand.adrDeparture
                 params["adrDestination"] = demand.adrDestination
                 params["departLongitude"] = demand.departLongitude.toString()
                 params["departLatitude"] = demand.departLatitude.toString()
                 params["destinationLongitude"] = demand.destinationLongitude.toString()
                 params["destinationLatitude"] = demand.destinationLatitude.toString()
                 params["nbrPassengers"] = demand.nbrPassengers.toString()
                 params["departTime"] = demand.departTime
                 params["departDate"] = demand.departDate
                 params["note"] = demand.Note*/
                //returning parameter
                return params
            }

        }
        requestQueue.add(request)


    }

    private fun mapArray(jsonResponse: JSONArray):ArrayList<OffreCovoiturage> {
        val myOrdersArrayList = ArrayList<OffreCovoiturage>()
        for(i in 0 until jsonResponse.length()){
            val jsonObject = jsonResponse.getJSONObject(i)
            val myOffer = OffreCovoiturage()
            myOffer.adrDeparture = jsonObject.getString("depart")
            myOffer.adrDestination = jsonObject.getString("destination")
            myOffer.departLatitude = jsonObject.getString("depart_longitude").toFloat()
            myOffer.departLongitude = jsonObject.getString("depart_latitude").toFloat()
            myOffer.destinationLatitude = jsonObject.getString("destination_longitude").toFloat()
            myOffer.destinationLongitude = jsonObject.getString("destination_latitude").toFloat()
            myOffer.freePlaces = jsonObject.getString("nbr_passengers").toInt()
            myOffer.price = jsonObject.getInt("cost_offer")
            myOffer.departTime = jsonObject.getString("heure")
            myOffer.departDate = jsonObject.getString("date")
            myOffer.uidOffer = jsonObject.getString("id_offer")

            myOrdersArrayList.add(myOffer)
        }

        return myOrdersArrayList
    }


}