package id.budi.agil.covid19.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.budi.agil.covid19.R
import id.budi.agil.covid19.api.ApiService
import id.budi.agil.covid19.model.AllCountries
import id.budi.agil.covid19.model.Countries
import id.budi.agil.covid19.model.DataCase
import id.budi.agil.covid19.model.TableDataCase
import id.budi.agil.covid19.model.World
import id.budi.agil.covid19.repository.CaseCovidRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application): ViewModel() {
    private val apiService = ApiService
    private val countryList = MutableLiveData<ArrayList<Countries>>()
    private val worldList = MutableLiveData<World>()
    private var countryFilterList = MutableLiveData<ArrayList<Countries>>()
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy")

    private val dataCaseList = MutableLiveData<ArrayList<DataCase>>()
    private val tableCaseList = MutableLiveData<ArrayList<TableDataCase>>()
    private val mCaseCovidRepository: CaseCovidRepository = CaseCovidRepository(application)
    init {
        countryFilterList = countryList
    }

    fun getAllTableDataCase(): LiveData<List<TableDataCase>> = mCaseCovidRepository.getAllTableDataCase()
    fun getCountryDataCase(countryCode: String): LiveData<List<DataCase>> = mCaseCovidRepository.getCountryDataCase(countryCode)

    fun dbEmpty(): Boolean = mCaseCovidRepository.dbEmpty()

    fun setSummary(query: String?){
        // mengambil data dari api melalui apiService
        apiService.getSummary().enqueue(object : Callback<AllCountries>{
            override fun onResponse(call: Call<AllCountries>, response: Response<AllCountries>) {
                if (response.isSuccessful) { // saat mendapatkan respond dari api
                    val body = response.body()
                    if(body != null){
                        val responseCountries: ArrayList<Countries> = body.Countries
                        val responseWorld = body.Global

                        if (query != null) {
                            // jika sedang mode pencarian
                            val resultList = ArrayList<Countries>()
                            for (row in responseCountries) {
                                if (row.Country!!.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                                    resultList.add(row)
                                }
                            }
                            countryList.postValue(resultList) // mengisi data countryList dengan data pencarian countries
                        } else {
                            // jika bukan mode pencarian atau normal
                            countryList.postValue(responseCountries) // mengisi data countryList dengan data semua countries
                        }

                        // mengubah format date yang diinginkan dari format date yang diterima dari api
                        val date: Date = inputFormat.parse(responseWorld.Date.toString()) as Date
                        val formattedDate: String = outputFormat.format(date)
                        val resultWorld = World(responseWorld.TotalConfirmed, responseWorld.TotalRecovered, responseWorld.TotalDeaths, formattedDate)

                        worldList.postValue(resultWorld) // mengisi data worldList semua data countries
                    }
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