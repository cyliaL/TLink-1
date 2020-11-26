package com.sirius.net.tlink.ui.covoiturageOffer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.sirius.net.tlink.adapters.CovOffersAdapter
import com.sirius.net.tlink.adapters.CovOrdersAdapter
import com.sirius.net.tlink.adapters.CovOrdersClick
import com.sirius.net.tlink.databinding.CovoiturageOfferFragmentBinding
import com.sirius.net.tlink.model.DateTime
import com.sirius.net.tlink.model.OffreCovoiturage
import com.sirius.net.tlink.model.OrderCovoiturage
import java.util.*


class CovoiturageOfferFragment : Fragment(),OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var currentDateTime = DateTime()
    var savedDateTime = DateTime()
    // lateinit var context: Context
    //private val viewModel: TaxiViewModel by activityViewModels()
    private lateinit var binding: CovoiturageOfferFragmentBinding
    private lateinit var mapView:MapView
    private lateinit var gMap:GoogleMap
    private val API_KEY = "AIzaSyCvYZHnDSX4RfKZp-zsZ5s91-_2H-7Fk-E"
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE = 123
    private val LOCATION_CHECK_CODE = 200
    private val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS)
    private var point = ""
    private var startMarker:Marker? = null
    private var endMarker:Marker? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.covoiturage_offer_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        checkLocationPermission()

        binding.confirmDirectionsCovOffer.setOnClickListener {
            //TODO post the request to the back en
            startDialog()
        }
        binding.backCovOffer.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.departAdrCovOffer.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setHint("Rechercher votre place")
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            point = "depart"
        }
        binding.detinationAdrCovOffer.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setHint("Rechercher votre place")
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            point = "destination"
        }
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
                //TODO get the position of the start marker and the end marker
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



    private fun startDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.info_offer_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        val backButtonDate = dialog.findViewById<ImageView>(R.id.back_day)
        //val note = dialog.findViewById<TextView>(R.id.note_demand)

        backButtonDate.setOnClickListener {
            /////
            dialog.dismiss()
        }
        dialog.show()

        val todayButton: Button = dialog.findViewById(R.id.today_button)
        val dateButton: Button = dialog.findViewById(R.id.date_button)

        todayButton.setOnClickListener {
            val cal = Calendar.getInstance()
            currentDateTime.hour = cal.get(Calendar.HOUR)
            currentDateTime.minute = cal.get(Calendar.MINUTE)
            TimePickerDialog(context, this, currentDateTime.hour, currentDateTime.minute, true).show()
            dialog.dismiss()
        }


        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            currentDateTime.day = cal.get(Calendar.DAY_OF_MONTH)
            currentDateTime.month = cal.get(Calendar.MONTH)
            currentDateTime.year = cal.get(Calendar.YEAR)
            currentDateTime.hour = cal.get(Calendar.HOUR)
            currentDateTime.minute = cal.get(Calendar.MINUTE)
            DatePickerDialog(
                requireContext(),
                this,
                currentDateTime.year,
                currentDateTime.month,
                currentDateTime.day
            ).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //TODO post the info to the backend
        savedDateTime.day = dayOfMonth
        savedDateTime.month = month
        savedDateTime.year = year
        TimePickerDialog(context, this, currentDateTime.hour, currentDateTime.minute, true).show()

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //TODO post the info to the backend and start the new fragment
        savedDateTime.hour = hourOfDay
        savedDateTime.minute = minute
        showOrdersList(ArrayList())

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

        val cancelButton = dialog.findViewById<Button>(R.id.cancel_search_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    //the dialog initialisation for orders list
    private fun showOrdersList(ordersList: ArrayList<OrderCovoiturage>){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.cov_order_select)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT
            , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val orderListRecycler = dialog.findViewById<RecyclerView>(R.id.covOrders_recycler)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        val backButtonOrders = dialog.findViewById<ImageView>(R.id.back_list_order)
        //val note = dialog.findViewById<TextView>(R.id.note_demand)

        backButtonOrders.setOnClickListener {
            startDialog()
            dialog.dismiss()
        }
        dialog.show()
        val click = object: CovOrdersClick {
            override fun onClick(position: Int) {
                infoOrder(position)
                dialog.dismiss()
            }

        }
        val adapter = CovOrdersAdapter(ordersList,click)

        orderListRecycler.setHasFixedSize(false)
        orderListRecycler.layoutManager = layoutManager
        orderListRecycler.adapter = adapter
        dialog.show()

    }
    private fun infoOrder(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.info_cov_order)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT
            , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val confirmButtonOrder = dialog.findViewById<Button>(R.id.confirm_cov_order_button)
        //val note = dialog.findViewById<TextView>(R.id.note_demand)

        confirmButtonOrder.setOnClickListener {
            finalConfirmation(position)
            dialog.dismiss()
        }
        val backButtonOrder = dialog.findViewById<ImageView>(R.id.back_image_order)
        //val note = dialog.findViewById<TextView>(R.id.note_demand)

        backButtonOrder.setOnClickListener {
            showOrdersList(ArrayList())
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun finalConfirmation(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.notes_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT
            , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val confirmButton = dialog.findViewById<Button>(R.id.confirm_note_button)
        //val note = dialog.findViewById<TextView>(R.id.note_demand)

        confirmButton.setOnClickListener {
            dialog.dismiss()
            requireActivity().onBackPressed()
        }
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        if(point == "depart"){
                            binding.departAdrCovOffer.text = place.name
                            startMarker?.position = place.latLng
                        }else if(point == "destination"){
                            binding.detinationAdrCovOffer.text = place.name
                            endMarker?.isVisible = true
                            endMarker?.position = place.latLng
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        Toast.makeText(requireContext()
                            ,"Erreur lors la recherche de la place, Réssayer s'il vous plait."
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