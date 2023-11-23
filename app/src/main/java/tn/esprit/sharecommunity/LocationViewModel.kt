package tn.esprit.sharecommunity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    val destinationText = MutableLiveData<String>()

    fun setDestinationText(text: String) {
        destinationText.value = text
    }
}