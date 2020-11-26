package com.sirius.net.tlink.ui.covoiturage


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.sirius.net.tlink.R
import com.sirius.net.tlink.databinding.CovoiturageFragmentBinding




class CovoiturageFragment : Fragment() {

    private val viewModel: CovoiturageViewModel by activityViewModels()
    private lateinit var binding: CovoiturageFragmentBinding
    private lateinit var bottomNav:BottomNavigationView
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.covoiturage_fragment, container, false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bottomNav = binding.bottomNavigation
        val navController = requireActivity().findNavController(R.id.fragment)

        bottomNav.setupWithNavController(navController)



    }


}



