package tn.esprit.sharecommunity

import EmDriverAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.sharecommunity.drivers.data.DriverData

class EmgCovoiturageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_emgcovoiturage, container, false)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val driverList = listOf(
            DriverData("Driver 1", "22 333 555", "Car 1", "7.500 DT"),
            // Add more items as needed
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewEMDrivers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = EmDriverAdapter(driverList)
        recyclerView.adapter = adapter
    }
}
