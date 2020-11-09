package com.sirius.net.tlink.ui.medical

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sirius.net.tlink.R

class TransportMedicalFragment : Fragment() {

    companion object {
        fun newInstance() = TransportMedicalFragment()
    }

    private lateinit var viewModel: TransportMedicalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.transport_medical_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TransportMedicalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}