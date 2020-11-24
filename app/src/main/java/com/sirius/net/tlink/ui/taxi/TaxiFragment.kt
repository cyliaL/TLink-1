package com.sirius.net.tlink.ui.taxi

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.sirius.net.tlink.R
import com.sirius.net.tlink.adapters.TaxiOffersAdapter
import com.sirius.net.tlink.adapters.TaxiOffersClick
import com.sirius.net.tlink.databinding.TaxiFragmentBinding
import com.sirius.net.tlink.model.DateTime
import com.sirius.net.tlink.model.OffreTaxi
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TaxiFragment : Fragment(),OnMapReadyCallback
        , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val viewModel: TaxiViewModel by activityViewModels()
    private val API_KEY = "AIzaSyCvYZHnDSX4RfKZp-zsZ5s91-_2H-7Fk-E"
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE = 123
    private val LOCATION_CHECK_CODE = 200
    private val fields = listOf(Place.Field.ID,Place.Field.ADDRESS, Place.Field.NAME,Place.Field.LAT_LNG)
    private var point = ""
    private var startMarker:Marker? = null
    private var endMarker:Marker? = null
    private var currentDateTime = DateTime()
    private var savedDateTime = DateTime()
    private lateinit var binding: TaxiFragmentBinding
    private lateinit var mapView:MapView
    private lateinit var gMap:GoogleMap
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPrefs:SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.taxi_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPrefs = requireContext().getSharedPreferences("TLINK", Context.MODE_PRIVATE)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        checkLocationPermission()
        requestQueue = Volley.newRequestQueue(requireContext())
        startDateTimeFlow()

        binding.confirmDirections.setOnClickListener {
            if(viewModel.currentTaxiDemand.adrDeparture.isNotEmpty() &&
                    viewModel.currentTaxiDemand.adrDestination.isNotEmpty()){
                val placeNum = binding.placeNum.text.toString()
                if(placeNum.isNotEmpty() && placeNum.toInt() <= 3){
                    viewModel.setPlacesnum(placeNum)
                    showNoteDialog()
                }else{
                    Toast.makeText(requireContext(),"Spécifier un nombre de places valid", LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),"Spécifier vos adresses de departure et destination", LENGTH_SHORT).show()
            }

        }

        binding.departAdr.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setHint("Rechercher votre place")
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            point = "depart"
        }

        binding.detinationAdr.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .setHint("Rechercher votre place")
                    .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            point = "destination"
        }
    }

    private fun startDateTimeFlow() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.date_choice_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT
                , ConstraintLayout.LayoutParams.MATCH_PARENT)

        val todayButton:Button = dialog.findViewById(R.id.today_button)
        val dateButton:Button = dialog.findViewById(R.id.date_button)

        val cal = Calendar.getInstance()
        currentDateTime.day = cal.get(Calendar.DAY_OF_MONTH)
        currentDateTime.month = cal.get(Calendar.MONTH)
        currentDateTime.year = cal.get(Calendar.YEAR)
        currentDateTime.hour = cal.get(Calendar.HOUR)
        currentDateTime.minute = cal.get(Calendar.MINUTE)

        todayButton.setOnClickListener {
            viewModel.setDemandDate(currentDateTime.day,currentDateTime.month,currentDateTime.year)
            viewModel.setDemandTime(currentDateTime.hour,currentDateTime.minute)
            dialog.dismiss()
        }

        dateButton.setOnClickListener {
            val dateDialog = DatePickerDialog(requireContext(),this,currentDateTime.year,currentDateTime.month,currentDateTime.day)
            dateDialog.setCancelable(false)
            dateDialog.setOnShowListener {
                dateDialog.getButton(Dialog.BUTTON_NEGATIVE).visibility = View.GONE
            }
            dateDialog.show()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDateTime.day = dayOfMonth
        savedDateTime.month = month
        savedDateTime.year = year
        viewModel.setDemandDate(savedDateTime.day,savedDateTime.month,savedDateTime.year)

        val timeDialog = TimePickerDialog(context,this,currentDateTime.hour,currentDateTime.minute,true)
        timeDialog.setCancelable(false)
        timeDialog.setOnShowListener {
            timeDialog.getButton(Dialog.BUTTON_NEGATIVE).visibility = View.GONE
        }
        timeDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedDateTime.hour = hourOfDay
        savedDateTime.minute = minute
        viewModel.setDemandTime(savedDateTime.hour,savedDateTime.minute)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        gMap = map!!
        gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.mapstyle))
        gMap.uiSettings?.isMyLocationButtonEnabled = false
        gMap.isMyLocationEnabled = true
        val algiers = LatLng(36.7525, 3.04197)
        startMarker = gMap.addMarker(
            MarkerOptions()
                .position(algiers)
                .title("Point de départ")
                .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true)
        )
        endMarker = gMap.addMarker(
            MarkerOptions()
                .position(algiers)
                .title("Destination")
                .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .draggable(true)
                .visible(false)
        )
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(algiers,10f))
        gMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker?) {}

            override fun onMarkerDrag(p0: Marker?) {}

            override fun onMarkerDragEnd(p0: Marker?) {
                //TODO add get the address full name
                if(!endMarker!!.isVisible){
                    endMarker!!.isVisible = true
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(endMarker!!.position))
                }
            }
        })
    }

    private fun checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext()
                        , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext()
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_REQUEST_CODE)
        }else{
            Places.initialize(requireContext(), API_KEY)
            mapView.getMapAsync(this)
            checkGps()
        }
    }

    private fun checkGps() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    // Show the dialog by calling startResolutionForResult().
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(requireActivity(),
                            LOCATION_CHECK_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

    }

    private fun showNoteDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.notes_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT
                , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val confirmButton = dialog.findViewById<Button>(R.id.confirm_note_button)
        val note = dialog.findViewById<EditText>(R.id.note_demand)

        confirmButton.setOnClickListener {
            viewModel.setDemandNote(note.text.toString())
            startSearch()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startSearch() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.searching_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT
                , ConstraintLayout.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(false)

        val url = "https://www.sirius-iot.eu/Dev/Tlink/Android_API_Taxi.php?signup_order"
        val request = object : StringRequest(Method.POST, url,
                {response ->
                    val jsonResponse = JSONObject(response)
                    val jsonObject = jsonResponse.getJSONObject("ORDER_REGISTRATION")
                    if(jsonObject.getString("error") == "false"){
                        showOffersList(ArrayList())
                    }else{
                        Toast.makeText(requireContext()
                                , jsonObject.getString("message"), LENGTH_LONG).show()
                    }
                    dialog.dismiss()
                },
                {error ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    requestQueue.stop()
                }
        ){
            override fun getParams():Map<String, String> {
                val params:HashMap<String,String> = HashMap()
                //Adding parameters to request
                val demand = viewModel.currentTaxiDemand
                params["idUser"] = sharedPrefs.getString("uid","")!!
                params["adrDeparture"] = demand.adrDeparture
                params["adrDestination"] = demand.adrDestination
                params["departLongitude"] = demand.departLongitude.toString()
                params["departLatitude"] = demand.departLatitude.toString()
                params["destinationLongitude"] = demand.destinationLongitude.toString()
                params["destinationLatitude"] = demand.destinationLatitude.toString()
                params["nbrPassengers"] = demand.nbrPassengers.toString()
                params["departTime"] = demand.departTime
                params["departDate"] = demand.departDate
                params["note"] = demand.Note
                //returning parameter
                return params;
            }

        }

        requestQueue.add(request)

        dialog.show()
    }

    private fun showOffersList(offersList: ArrayList<OffreTaxi>){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.offer_selection_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT
                , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val offerListRecycler = dialog.findViewById<RecyclerView>(R.id.offers_recycler)
        val layoutManager = LinearLayoutManager(requireContext(),VERTICAL,false)
        val click = object: TaxiOffersClick{
            override fun onClick(position: Int) {
                dialog.dismiss()
            }
        }
        val adapter = TaxiOffersAdapter(offersList,click)

        offerListRecycler.setHasFixedSize(false)
        offerListRecycler.layoutManager = layoutManager
        offerListRecycler.adapter = adapter
        dialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        if(point == "depart"){
                            binding.departAdr.text = place.name
                            startMarker?.position = place.latLng
                            viewModel.setDepartPlace(place)
                        }else if(point == "destination"){
                            binding.detinationAdr.text = place.name
                            endMarker?.isVisible = true
                            endMarker?.position = place.latLng
                            viewModel.setDestinationPlace(place)
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Toast.makeText(requireContext()
                                ,status.statusMessage
                                , LENGTH_LONG).show()
                    }
                }
                Activity.RESULT_CANCELED -> { }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Places.initialize(requireContext(), API_KEY)
                mapView.getMapAsync(this)
                checkGps()
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(
                        requireContext(), "L'application doit avoir votre localisation pour fonctionner."
                        , LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

   override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}