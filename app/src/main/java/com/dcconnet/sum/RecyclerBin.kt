package com.dcconnet.sum

data class RecyclerBin(
    val binStatus: String? = "Boş",
    val title: String? = "",
    val lat: Double? = 0.0,
    val lon: Double? = 0.0,
    val statusGlass: Boolean? = false,
    val statusPaper: Boolean? = false,
    val statusPlastic: Boolean? = false,
    val typeGlass: Boolean? = false,
    val typePaper: Boolean? = false,
    val typePlastic: Boolean? = false,
    val volume: Int? = 50,
    val currentTime: String? = ""
)
