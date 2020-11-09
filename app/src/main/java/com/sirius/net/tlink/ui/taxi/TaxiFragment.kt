package com.sirius.net.tlink.ui.taxi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sirius.net.tlink.R

class TaxiFragment : Fragment() {

    companion object {
        fun newInstance() = TaxiFragment()
    }

    private lateinit var viewModel: TaxiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.taxi_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TaxiViewModel::class.java)
        // TODO: Use the ViewModel
    }

}