package com.sirius.net.tlink.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.sirius.net.tlink.R
import com.sirius.net.tlink.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()
    private lateinit var binding: RegisterFragmentBinding
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.register_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerButton = binding.registerButton

        //TODO faire la registration et naviguer vers la confirmation
        registerButton.setOnClickListener {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_login)
            navController.navigate(R.id.nav_confirm_register)
        }

    }

}