package com.sirius.net.tlink.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.sirius.net.tlink.R
import com.sirius.net.tlink.adapters.OrdersAdapter
import com.sirius.net.tlink.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {

    private val viewModel: OrderViewModel by activityViewModels()
    private lateinit var binding: FragmentOrderBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.optionsRecycler
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2,VERTICAL,false)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = OrdersAdapter(requireActivity().findNavController(R.id.nav_host_fragment))
    }
}