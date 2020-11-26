package com.sirius.net.tlink.ui.covoiturageMyOffers

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.adapters.CovMyOffersAdapter

import com.sirius.net.tlink.databinding.CovoiturageMyOffersFragmentBinding

import com.sirius.net.tlink.model.Historique

class CovoiturageMyOffersFragment : Fragment() {


    //private val viewModel: CovoiturageMyOffersViewModel by activityViewModels()
    private lateinit var binding: CovoiturageMyOffersFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.covoiturage_my_offers_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // showMyOffersList(ArrayList())
        val myOfferListRecycler = view?.findViewById<RecyclerView>(R.id.covMyOffers_recycler)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        val adapter = CovMyOffersAdapter(ArrayList())

        myOfferListRecycler?.setHasFixedSize(false)
        myOfferListRecycler?.layoutManager = layoutManager
        myOfferListRecycler?.adapter = adapter

    }

    private fun startSearch() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.searching_dialog_cov)
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
    private fun showMyOffersList(myOffersList: ArrayList<Historique>){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.covoiturage_my_offers_fragment)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT
            , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val myOfferListRecycler = dialog.findViewById<RecyclerView>(R.id.covMyOffers_recycler)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        val adapter = CovMyOffersAdapter(myOffersList)

        myOfferListRecycler.setHasFixedSize(false)
        myOfferListRecycler.layoutManager = layoutManager
        myOfferListRecycler.adapter = adapter
        dialog.show()

    }

}