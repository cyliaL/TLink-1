package com.sirius.net.tlink.ui.marchandise

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sirius.net.tlink.R

class MarchandiseFragment : Fragment() {

    companion object {
        fun newInstance() = MarchandiseFragment()
    }

    private lateinit var viewModel: MarchandiseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.marchandise_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MarchandiseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}