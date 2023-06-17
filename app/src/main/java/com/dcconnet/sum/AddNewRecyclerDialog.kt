package com.dcconnet.sum

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import androidx.cardview.widget.CardView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class AddNewRecyclerDialog(context: Context) : Dialog(context) {

    private lateinit var titleEt: EditText
    private lateinit var submitButton: CardView
    private lateinit var chipGroup: ChipGroup

    var onButtonClickListener: ((List<Chip>, LatLng, String) -> Unit)? = null
    private var latLon: LatLng = LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_new_recycler)

        titleEt = findViewById(R.id.titleEt)
        submitButton = findViewById(R.id.select_bins_button)
        chipGroup = findViewById(R.id.chipGroup)

        submitButton.setOnClickListener {
            if (!titleEt.text.toString().isNullOrEmpty()) {
                onButtonClickListener?.invoke(
                    chipGroup.getSelectedChips(),
                    latLon,
                    titleEt.text.toString()
                )
                dismiss()
            }
        }
    }

    private fun ChipGroup.getSelectedChips(): List<Chip> {
        val selectedChips = mutableListOf<Chip>()

        for (i in 0 until childCount) {
            val chip = getChildAt(i) as? Chip
            if (chip?.isChecked == true) {
                selectedChips.add(chip)
            }
        }
        return selectedChips
    }

    fun setLatLon(latLon: LatLng) {
        this.latLon = latLon
    }
}