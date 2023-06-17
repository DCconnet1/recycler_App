package com.dcconnet.sum

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dcconnet.sum.databinding.RowTrashBinBinding

class TrashBinAdapter : ListAdapter<RecyclerBin, TrashBinViewHolder>(DIFF_CALL_BACK) {
    var onButtonClickListener : ((RecyclerBin) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashBinViewHolder {
        val binding = RowTrashBinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrashBinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrashBinViewHolder, position: Int) {
        holder.bind(getItem(position), onButtonClickListener)
    }


    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<RecyclerBin>() {
            override fun areItemsTheSame(oldItem: RecyclerBin, newItem: RecyclerBin): Boolean {
                return oldItem.lat == newItem.lat
            }

            override fun areContentsTheSame(oldItem: RecyclerBin, newItem: RecyclerBin): Boolean {
                return oldItem == newItem
            }

        }
    }
}

class TrashBinViewHolder(val binding: RowTrashBinBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(item: RecyclerBin?, onButtonClickListener: ((RecyclerBin) -> Unit)?) {
        binding.recyclerBinItems.text = getExistedBinsString(item)
        binding.recyclerBinName.text = item?.title
        binding.cardItemsText.text = "Hacimi : ${item?.volume}"
        binding.currentStatusText.text = item?.binStatus
        binding.changeStatusButton.setOnClickListener{
            item?.let { mItem -> onButtonClickListener?.invoke(mItem) }
        }
    }

    private fun getExistedBinsString(item: RecyclerBin?): String {
        var bins = ""
        if (item?.typePaper == true) {
            bins = "Kağıt"
        }
        if (item?.typeGlass == true) {
            bins = if (bins.isNotEmpty()) {
                "$bins, Cam"
            } else {
                "Cam"
            }
        }
        if (item?.typePlastic == true) {
            bins = if (bins.isNotEmpty()) {
                "$bins, Plastik"
            } else {
                "Plastik"
            }
        }
        return bins
    }

}
