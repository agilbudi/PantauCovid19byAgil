package id.budi.agil.covid19.api

import id.budi.agil.covid19.model.AllCountries
import id.budi.agil.covid19.model.InfoCountry
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://api.covid19api.com/"
    private val api:CovidApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(CovidApi::class.java)
    }

    fun getSummary(): Call<AllCountries>{
        return api.getAllCountries()
    }
    fun getCountry(country:String): Call<ArrayList<InfoCountry>>{
        return api.getInfoCountry(country)
    }
}