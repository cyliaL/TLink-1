package com.sirius.net.tlink.ui.login

import android.content.Intent
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
import com.sirius.net.tlink.activities.Main.MainActivity
import com.sirius.net.tlink.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var binding: LoginFragmentBinding
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(layoutInflater, R.layout.login_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginButton = binding.loginButton
        registerButton = binding.registerLaunchButton


        //TODO: fair le login
        loginButton.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),MainActivity::class.java)).also {
                requireActivity().finish()
            }
        }

        //TODO naviguer vers registration
        registerButton.setOnClickListener {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_login)
            navController.navigate(R.id.action_register)
        }
    }

}