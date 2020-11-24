package com.sirius.net.tlink.ui.login

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sirius.net.tlink.R
import com.sirius.net.tlink.activities.Main.MainActivity
import com.sirius.net.tlink.databinding.LoginFragmentBinding
import org.json.JSONObject

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var binding: LoginFragmentBinding
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var forgotPassButton: Button
    private lateinit var phoneNumberInput: TextView
    private lateinit var passwordInput: TextView
    private lateinit var requestQueue: RequestQueue
    private lateinit var SharedPrefs:SharedPreferences
    private lateinit var dialog:Dialog

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
        dialog = Dialog(requireContext())
        SharedPrefs = requireContext().getSharedPreferences("TLINK",MODE_PRIVATE)
        if(SharedPrefs.getBoolean("IS_USER_LOGED",false)){
            val intent  = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent).also {
                requireActivity().finish()
            }
        }
        requestQueue = Volley.newRequestQueue(requireContext())

        loginButton = binding.loginButton
        registerButton = binding.registerLaunchButton
        phoneNumberInput = binding.loginPhoneInput
        passwordInput = binding.loginPasswordInput
        forgotPassButton = binding.forgottenPassword

        loginButton.setOnClickListener {
            verifyInputs()
        }

        forgotPassButton.setOnClickListener {
            verifyPhoneInput()
        }

        registerButton.setOnClickListener {
            val navController = requireActivity()
                    .findNavController(R.id.nav_host_fragment_login)
            navController.navigate(R.id.action_register)
        }
    }

    private fun verifyPhoneInput() {
        val phoneNumber = phoneNumberInput.text.toString()
        if(phoneNumber.isNotEmpty()){
            showLoading()
            sendReset(phoneNumber)
        }
    }

    private fun sendReset(phoneNumber: String) {
        val url = "https://www.sirius-iot.eu/Dev/Tlink/Android_API_All.php?forgot_password"
        val request = object : StringRequest(Method.POST, url,
                {response ->
                    val jsonResponse = JSONObject(response)
                    val jsonObject = jsonResponse.getJSONObject("forgot_password")
                    if(jsonObject.getString("error") == "false"){
                        Toast.makeText(requireContext()
                                , "vous etes en train de recevez une " +
                                "appelle telephonique pour confirmer", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(requireContext()
                                , jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                    }
                    dialog.dismiss()
                },
                {error ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    requestQueue.stop()
                }
        ){
            override fun getParams():Map<String, String> {
                val params:HashMap<String,String> = HashMap()
                //Adding parameters to request
                params["num_tel"] = phoneNumber
                //returning parameter
                return params;
            }

        }

        requestQueue.add(request)
    }

    private fun showLoading() {
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    /**
     * pour verifier les input est ce qu'il ne sont pas vides
     */
    private fun verifyInputs() {
        val phoneNumber = phoneNumberInput.text.toString()
        val password = passwordInput.text.toString()

        if(password.isNotEmpty() && phoneNumber.isNotEmpty()){
            showLoading()
            signIn(phoneNumber,password)
        }
    }

    /**
     * faire la requet de login
     */
    private fun signIn(userPhone: String, password: String) {

        val url = "https://www.sirius-iot.eu/Dev/Tlink/Android_API_All.php?login_user"
        val request = object : StringRequest(Method.POST, url,
                {response ->
                    val jsonResponse = JSONObject(response)
                    val jsonObject = jsonResponse.getJSONObject("LOGGED_IN_USER")
                    if(jsonObject.getString("error") == "false"){
                        handleRequest(jsonObject)
                    }else{
                        dialog.dismiss()
                        Toast.makeText(requireContext()
                                , jsonObject.getString("msg"), Toast.LENGTH_LONG).show()
                    }
                },
                {error ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    requestQueue.stop()
                }
        ){
            override fun getParams():Map<String, String> {
                val params:HashMap<String,String> = HashMap()
                //Adding parameters to request
                params["telephone"] = userPhone
                params["password"] = password
                //returning parameter
                return params;
            }

        }

        requestQueue.add(request)
    }

    private fun handleRequest(jsonObject: JSONObject) {
        Toast.makeText(requireContext()
                , "Bienvenu ${jsonObject.getString("name")}", Toast.LENGTH_LONG).show()
        SharedPrefs.edit().putBoolean("IS_USER_LOGED",true).apply()
        SharedPrefs.edit().putString("name",jsonObject.getString("name")).apply()
        SharedPrefs.edit().putString("surname",jsonObject.getString("surname")).apply()
        SharedPrefs.edit().putString("mail",jsonObject.getString("mail")).apply()
        SharedPrefs.edit().putString("tel1",jsonObject.getString("tel1")).apply()
        SharedPrefs.edit().putString("tel2",jsonObject.getString("tel2")).apply()
        SharedPrefs.edit().putString("uid",jsonObject.getString("uid")).apply()

        val intent  = Intent(requireActivity(),MainActivity::class.java)
        startActivity(intent).also {
            dialog.dismiss()
            requireActivity().finish()
        }
    }

}