package id.budi.agil.covid19.api

import id.budi.agil.covid19.model.AllCountries
import id.budi.agil.covid19.model.InfoCountry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidApi {
    @GET("summary") // menjadi https://api.covid19api.com/summary
    fun getAllCountries(): Call<AllCountries> // fungsi mengambil data api dengan url https://api.covid19api.com/summary

    @GET("dayone/country/{country}") // menjadi https://api.covid19api.com/dayone/country/indonesia #misal negara indonesia
    fun getInfoCountry(@Path("country") country:String?): Call<ArrayList<InfoCountry>> // fungsi mengambil data api dengan url https://api.covid19api.com/dayone/country/{country}
}