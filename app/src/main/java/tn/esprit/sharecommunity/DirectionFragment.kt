package tn.esprit.sharecommunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class DirectionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_direction, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         // val destinationEditText = view.findViewById<EditText>(R.id.DestinationEditText)
        // val destinationLayout = view.findViewById<LinearLayout>(R.id.DestinationLayout)
        val locationMap = view.findViewById<TextView>(R.id.locationMap)



        locationMap.setOnClickListener {
              requireActivity().supportFragmentManager.commit {
                replace(R.id.fragment_container, MapFragment())
                addToBackStack(null)
            }
        }

    }

}