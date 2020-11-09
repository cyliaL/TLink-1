package com.sirius.net.tlink.ui.covoiturage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sirius.net.tlink.R

class CovoiturageFragment : Fragment() {

    companion object {
        fun newInstance() = CovoiturageFragment()
    }

    private lateinit var viewModel: CovoiturageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.covoiturage_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CovoiturageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}