package com.sirius.net.tlink.ui.register

import android.app.Dialog
import android.content.Context
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
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sirius.net.tlink.R
import com.sirius.net.tlink.activities.Main.MainActivity
import com.sirius.net.tlink.databinding.RegisterFragmentBinding
import org.json.JSONObject

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()
    private lateinit var binding: RegisterFragmentBinding
    private lateinit var registerButton: Button
    private lateinit var username:EditText
    private lateinit var userSurname:EditText
    private lateinit var userPhone:EditText
    private lateinit var userPassword:EditText
    private lateinit var confirmUserPassword:EditText
    private lateinit var termCheckBox: CheckBox
    private lateinit var requestQueue: RequestQueue
    private lateinit var SharedPrefs:SharedPreferences
    private lateinit var dialog:Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.register_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog = Dialog(requireContext())
        SharedPrefs = requireContext().getSharedPreferences("TLINK", MODE_PRIVATE)
        requestQueue = Volley.newRequestQueue(requireContext())
        registerButton = binding.registerButton
        username = binding.registerUsernameInput
        userSurname = binding.registerUsersurnameInput
        userPhone = binding.registerPhoneInput
        userPassword = binding.registerPasswordInput
        confirmUserPassword = binding.registerPasswordConfirm
        termCheckBox = binding.termCheckbox

        registerButton.setOnClickListener {
            showLoading()
            performRegister()
        }

    }

    private fun showLoading() {
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun performRegister() {
        val name = username.text.toString()
        val surname = userSurname.text.toString()
        val phone = userPhone.text.toString()
        val password = userPassword.text.toString()
        val confirmPass = confirmUserPassword.text.toString()
        val url = "https://www.sirius-iot.eu/Dev/Tlink/Android_API_All.php?signup_user"

        if(termCheckBox.isChecked){
            if(name.isNotEmpty() && surname.isNotEmpty()
                    && phone.isNotEmpty() && password.isNotEmpty()
                    && confirmPass.isNotEmpty()){
                if(password == confirmPass){
                    val request = object : StringRequest(Method.POST, url,
                            {response ->
                                val jsonResponse = JSONObject(response)
                                val jsonObject = jsonResponse
                                        .getJSONObject("USER_REGISTRATION")
                                if(jsonObject.getString("error") == "false"){
                                    handleRequest(jsonObject)
                                }
                            },
                            {error ->
                                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                                requestQueue.stop()
                                dialog.dismiss()
                            }
                    ){
                        override fun getParams():Map<String, String> {
                            val params:HashMap<String,String> = HashMap()
                            //Adding parameters to request
                            params["name"] = name
                            params["surname"] = surname
                            params["tel1"] = phone
                            params["password"] = password
                            params["accept_cond_term"] = "1"
                            //returning parameter
                            return params
                        }

                    }

                    requestQueue.add(request)
                }else{
                    dialog.dismiss()
                    Toast.makeText(requireContext()
                            , getString(R.string.warning_password),LENGTH_SHORT)
                            .show()
                }
            }

        }
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

        val intent  = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent).also {
            dialog.dismiss()
            requireActivity().finish()
        }
    }

}