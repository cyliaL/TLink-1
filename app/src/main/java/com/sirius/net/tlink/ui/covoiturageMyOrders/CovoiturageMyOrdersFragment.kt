package com.sirius.net.tlink.ui.covoiturageMyOrders

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.adapters.CovMyOrderAdapter
import com.sirius.net.tlink.adapters.CovOrdersAdapter
import com.sirius.net.tlink.databinding.CovoiturageMyOrdersFragmentBinding
import com.sirius.net.tlink.model.Historique
import kotlin.collections.ArrayList

class CovoiturageMyOrdersFragment : Fragment() {


    //private val viewModel: TaxiViewModel by activityViewModels()
    private lateinit var binding: CovoiturageMyOrdersFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.covoiturage_my_orders_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // showMyOrdersList(ArrayList())
        val myOrderListRecycler = view?.findViewById<RecyclerView>(R.id.covMyOrders_recycler)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        val adapter = CovMyOrderAdapter(ArrayList())

        myOrderListRecycler?.setHasFixedSize(false)
        myOrderListRecycler?.layoutManager = layoutManager
        myOrderListRecycler?.adapter = adapter

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
    private fun showMyOrdersList(myOrdersList: ArrayList<Historique>){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.covoiturage_my_orders_fragment)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT
            , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val myOrderListRecycler = dialog.findViewById<RecyclerView>(R.id.covMyOrders_recycler)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        val adapter = CovMyOrderAdapter(myOrdersList)

        myOrderListRecycler.setHasFixedSize(false)
        myOrderListRecycler.layoutManager = layoutManager
        myOrderListRecycler.adapter = adapter
        dialog.show()

    }

}