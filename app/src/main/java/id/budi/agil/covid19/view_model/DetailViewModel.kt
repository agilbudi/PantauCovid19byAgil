package id.budi.agil.covid19.view_model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import id.budi.agil.covid19.api.ApiService
import id.budi.agil.covid19.model.ChartCases
import id.budi.agil.covid19.model.InfoCountry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailViewModel: ViewModel() {
    private val apiService = ApiService
    private val dataCases = MutableLiveData<ChartCases>()

    fun setDataCases(slug: String){
        apiService.getCountry(slug).enqueue(object : Callback<ArrayList<InfoCountry>>{
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<ArrayList<InfoCountry>>, response: Response<ArrayList<InfoCountry>>) {
                if (response.isSuccessful) {
                    val dataResponse = response.body()
                    val lineConfirmed: ArrayList<Entry> = ArrayList()
                    val lineRecovered: ArrayList<Entry> = ArrayList()
                    val lineActive: ArrayList<Entry> = ArrayList()
                    val lineDeaths: ArrayList<Entry> = ArrayList()
                    val dayCases = ArrayList<String>()
                    if (dataResponse != null) {
                        var i = 0
                        while (i < dataResponse.size) {
                            for (data in dataResponse) {
                                val entryConfirmed = Entry(i.toFloat(), data.Confirmed!!.toFloat())
                                val entryRecovered = Entry(i.toFloat(), data.Recovered!!.toFloat())
                                val entryActive = Entry(i.toFloat(), data.Active!!.toFloat())
                                val entryDeaths = Entry(i.toFloat(), data.Deaths!!.toFloat())

                                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS'Z'")
                                val outputFormat = SimpleDateFormat("dd-MM-yyyy")
                                val date: Date? = inputFormat.parse(data.Date.toString())
                                val formattedDate: String = outputFormat.format(date!!)
                                dayCases.add(formattedDate)
                                lineConfirmed.add(entryConfirmed)
                                lineRecovered.add(entryRecovered)
                                lineActive.add(entryActive)
                                lineDeaths.add(entryDeaths)
                                i++
                            }
                        }
                    }
                    val result = ChartCases(lineConfirmed, lineRecovered, lineActive, lineDeaths, dayCases)
                    dataCases.postValue(result)
                }else{
                    Log.e("ON_CHART_DATA_RESPONSE", response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<InfoCountry>>, t: Throwable) {
                Log.e("ON_CHART_DATA_FAILURE", t.message.toString())
            }
        })
    }

    fun getDataCases(): LiveData<ChartCases>{
        return dataCases
    }
}