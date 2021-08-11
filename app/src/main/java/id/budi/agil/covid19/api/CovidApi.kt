package id.budi.agil.covid19.api

import id.budi.agil.covid19.model.AllCountries
import id.budi.agil.covid19.model.InfoCountry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidApi {
    @GET("summary")
    fun getAllCountries(): Call<AllCountries>

    @GET("dayone/country/{country}")
    fun getInfoCountry(@Path("country") country:String?): Call<ArrayList<InfoCountry>>
}