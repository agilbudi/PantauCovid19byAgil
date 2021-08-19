package id.budi.agil.covid19.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.budi.agil.covid19.R
import id.budi.agil.covid19.api.ApiService
import id.budi.agil.covid19.model.AllCountries
import id.budi.agil.covid19.model.Countries
import id.budi.agil.covid19.model.World
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {
    private val apiService = ApiService
    private val countryList = MutableLiveData<ArrayList<Countries>>()
    private val worldList = MutableLiveData<World>()
    private var countryFilterList = MutableLiveData<ArrayList<Countries>>()
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy")
    init {
        countryFilterList = countryList
    }

    fun setSummary(query: String?){
        // mengambil data dari api melalui apiService
        apiService.getSummary().enqueue(object : Callback<AllCountries>{
            override fun onResponse(call: Call<AllCountries>, response: Response<AllCountries>) {
                if (response.isSuccessful) { // saat mendapatkan respond dari api
                    val responseCountries: ArrayList<Countries>? = response.body()?.Countries
                    val responseWorld = response.body()?.Global

                    if (query != null) {
                        // jika sedang mode pencarian
                        val resultList = ArrayList<Countries>()
                        if (responseCountries != null) {
                            for (row in responseCountries) {
                                if (row.Country!!.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                                    resultList.add(row)
                                }
                            }
                            countryList.postValue(resultList) // mengisi data countryList dengan data pencarian countries
                        }
                    } else {
                        // jika bukan mode pencarian atau normal
                        countryList.postValue(responseCountries) // mengisi data countryList dengan data semua countries
                    }

                    // mengubah format date yang diinginkan dari format date yang diterima dari api
                    val date: Date? = inputFormat.parse(responseWorld?.Date.toString())
                    val formattedDate: String = outputFormat.format(date!!)
                    val resultWorld = World(responseWorld?.TotalConfirmed, responseWorld?.TotalRecovered, responseWorld?.TotalDeaths, formattedDate)

                    worldList.postValue(resultWorld) // mengisi data worldList semua data countries
                }else{
                    Log.e("ON_SUMMARY_RESPONSE", response.message())
                }
            }

            override fun onFailure(call: Call<AllCountries>, t: Throwable) {
                Log.e("ON_SUMMARY_FAILURE", t.message.toString())
            }
        })
    }

    fun getSorted(by: Int) {
        when(by){
            R.id.main_rb_reset ->{
                countryList.value?.sortBy { it.Country } // sorting berdasarkan nama Country
            }
            R.id.main_rb_confirmed ->{
                countryList.value?.sortByDescending { it.TotalConfirmed?.toInt() } // sorting berdasarkan TotalConfirmed yang terbasar
            }
            R.id.main_rb_recovered ->{
                countryList.value?.sortByDescending { it.TotalRecovered?.toInt() } //sorting berdasarkan TotalRecovered yang terbasar
            }
            R.id.main_rb_death ->{
                countryList.value?.sortByDescending { it.TotalDeaths?.toInt() } // sorting berdasarkan TotalDeaths yang terbasar
            }
        }
    }

    fun getCountries(): LiveData<ArrayList<Countries>>{ // mengambil data yang ada di countryList
        return countryList
    }
    fun getWorld(): LiveData<World>{ // mengambil data yang ada di worldList
        return worldList
    }
}