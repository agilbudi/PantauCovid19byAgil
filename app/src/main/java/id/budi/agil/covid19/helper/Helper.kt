package id.budi.agil.covid19.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {
    fun getCountryCode(countryName: String) =
        Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}