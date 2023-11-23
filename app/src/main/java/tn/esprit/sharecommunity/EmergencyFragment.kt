package tn.esprit.sharecommunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class EmergencyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_emergency, container, false)

        // Find the LinearLayout by its ID
        val linearLayout = view.findViewById<LinearLayout>(R.id.emergencyBtn)

        // Set an OnClickListener for the LinearLayout
        linearLayout.setOnClickListener {
            // Perform the action you want when the LinearLayout is clicked
            // For example, navigate to another fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragment_container, CarmapFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }
}

