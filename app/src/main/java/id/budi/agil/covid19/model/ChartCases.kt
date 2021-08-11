package id.budi.agil.covid19.model

import com.github.mikephil.charting.data.Entry

data class ChartCases(
    val dataConfirmed: ArrayList<Entry>,
    val dataRecovered: ArrayList<Entry>,
    val dataActive: ArrayList<Entry>,
    val dataDeaths: ArrayList<Entry>,
    val dateCases: ArrayList<String>
)
