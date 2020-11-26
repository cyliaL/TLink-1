package com.sirius.net.tlink.ui.covoiturageNouvelle


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.adapters.CovoiturageAdapter
import com.sirius.net.tlink.databinding.CovoiturageNouvelleFragmentBinding
import com.sirius.net.tlink.ui.covoiturage.CovoiturageViewModel

class CovoiturageNouvelleFragment : Fragment() {

    private val viewModel: CovoiturageViewModel by activityViewModels()
    private lateinit var binding: CovoiturageNouvelleFragmentBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.covoiturage_nouvelle_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.optionsNew
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1,
            RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = CovoiturageAdapter(requireActivity().findNavController(R.id.fragment))
    }
}
