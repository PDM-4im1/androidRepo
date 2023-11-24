package tn.esprit.sharecommunity

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView

class DriverPopupDialog(
    private val context: Context,
    private val source: String,
    private val destination: String,
    private val price: String
) {

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.driver_popup_layout, null)
        val textSource = dialogView.findViewById<TextView>(R.id.textSource)
        val textDestination = dialogView.findViewById<TextView>(R.id.textDestination)
        val textPrice = dialogView.findViewById<TextView>(R.id.textPrice)
        val btnAccept = dialogView.findViewById<Button>(R.id.btnAccept)
        val btnRefuse = dialogView.findViewById<Button>(R.id.btnRefuse)

        textSource.text = "Source: $source"
        textDestination.text = "Destination: $destination"
        textPrice.text = "Price: $price"

        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = builder.create()

        btnAccept.setOnClickListener {
            // Handle accept button click
            alertDialog.dismiss()
            // Your logic here...
        }

        btnRefuse.setOnClickListener {
            // Handle refuse button click
            alertDialog.dismiss()
            // Your logic here...
        }

        alertDialog.show()
    }
}

