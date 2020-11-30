package com.sirius.net.tlink.ui.covoiturageMyOrders

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
import com.sirius.net.tlink.adapters.CovMyOrderAdapter
import com.sirius.net.tlink.databinding.CovoiturageMyOrdersFragmentBinding
import com.sirius.net.tlink.model.OrderCovoiturage
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

class CovoiturageMyOrdersFragment : Fragment() {


    private val viewModel: CovoiturageMyOrdersFragment by activityViewModels()
    private lateinit var binding: CovoiturageMyOrdersFragmentBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.covoiturage_my_orders_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPrefs = requireContext().getSharedPreferences("TLINK", Context.MODE_PRIVATE)
        requestQueue = Volley.newRequestQueue(requireContext())
        showMyOrdersList()
    }

    private fun showMyOrdersList(){

        val url = "https://www.sirius-iot.eu/Dev/Tlink/Android_API_Covtrg.php?histrq_orders"
        val request = object : StringRequest(Method.POST, url,
                { response ->
                    val jsonResponse = JSONObject(response)
                    val jsonObject = jsonResponse.getJSONObject("ORDERS_HISTORIQ")
                    if (jsonObject.getString("error") == "false") {
                       // viewModel.setIdDemand(jsonObject.getString("id_order"))

                        val adapter = CovMyOrderAdapter( mapArray(jsonObject.getJSONArray("LIST")))

                        val myOrderListRecycler = view?.findViewById<RecyclerView>(R.id.covMyOrders_recycler)
                        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
                        myOrderListRecycler?.setHasFixedSize(false)
                        myOrderListRecycler?.layoutManager = layoutManager
                        myOrderListRecycler?.adapter = adapter
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

    private fun mapArray(jsonResponse: JSONArray):ArrayList<OrderCovoiturage> {
        val myOrdersArrayList = ArrayList<OrderCovoiturage>()
        for(i in 0 until jsonResponse.length()){
            val jsonObject = jsonResponse.getJSONObject(i)
            val myOrder = OrderCovoiturage()
            myOrder.adrDeparture = jsonObject.getString("depart")
            myOrder.adrDestination = jsonObject.getString("destination")
            myOrder.departLatitude = jsonObject.getString("depart_longitude").toFloat()
            myOrder.departLongitude = jsonObject.getString("depart_latitude").toFloat()
            myOrder.destinationLatitude = jsonObject.getString("destination_longitude").toFloat()
            myOrder.destinationLongitude = jsonObject.getString("destination_latitude").toFloat()
            myOrder.nbrPassengers = jsonObject.getString("nbr_passengers").toInt()
            //myOrder.price = jsonObject.getInt("cost_offer")
            myOrder.departTime = jsonObject.getString("heure")
            myOrder.departDate = jsonObject.getString("date")
            myOrder.uidDemand = jsonObject.getString("id_order")

            myOrdersArrayList.add(myOrder)
        }

        return myOrdersArrayList
    }


}