package id.budi.agil.covid19.api

import id.budi.agil.covid19.model.AllCountries
import id.budi.agil.covid19.model.InfoCountry
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://api.covid19api.com/"
    private val api:CovidApi by lazy {
        // membuat request api dengan base url
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(CovidApi::class.java)
    }

    fun getSummary(): Call<AllCountries>{
        // request api dengan AllCountries
        return api.getAllCountries()
    }
    fun getCountry(country:String): Call<ArrayList<InfoCountry>>{
        // request api dengan country
        return api.getInfoCountry(country)
    }
}