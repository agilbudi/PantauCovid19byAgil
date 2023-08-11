package id.budi.agil.covid19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.budi.agil.covid19.adapter.MainAdapter
import id.budi.agil.covid19.databinding.ActivityMainBinding
import id.budi.agil.covid19.model.Countries
import id.budi.agil.covid19.view_model.MainViewModel
import id.budi.agil.covid19.view_model.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val mainAdapter = MainAdapter()
    private lateinit var dateWorld: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainViewModel = obtainViewModel(this@MainActivity)

//        GlobalData 	= https://covid19.who.int/WHO-COVID-19-global-data.csv
//        GlobalTableData = https://covid19.who.int/WHO-COVID-19-global-table-data.csv

        if (mainViewModel.dbEmpty()){
            //TODO download data & save data
        }


//
//        val searchView = binding.mainSv
//        // inisiasi View Model
//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
//
//        // konfigurasi recyclerview
//        binding.mainRvCountries.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context)
//            adapter = mainAdapter
//        }
//        showLoading(true)
//        // konfigurasi kolom pencarian
//        searchView.queryHint = resources.getString(R.string.search)
//        initSearch(searchView)
//
//        // kondisi jika data masih kosong
//        if(viewModel.getCountries().value == null){
//            showLoading(true)
//            showData(null)
//        }
//        // konfigurasi radiobutton
//        binding.mainRbReset.isChecked = true
//        binding.mainRg.setOnCheckedChangeListener{ _, checkedId ->
//            // sorting dengan mengirim id radio button yang terpilih
//            sortBy(checkedId)
//        }
//
//        // konfigurasi favorite action button
//        binding.mainFabMenu.setOnClickListener {
//            val popupMenu = PopupMenu(this, binding.mainFabMenu)
//            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
//            popupMenu.setOnMenuItemClickListener {item ->
//                when(item.itemId){
//                    R.id.menu_theme_light -> changeTheme(false)
//                    R.id.menu_theme_night -> changeTheme(true)
//                    else -> changeTheme(false)
//                }
//            }
//            popupMenu.show()
//        }
//
//        // konfigurasi action klik setiap item di recyclerview
//        mainAdapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback{
//            override fun onItemClicked(data: Countries) {
//                // mengirimkan data yang diklik
//                selectedItem(data)
//            }
//        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    private fun sortBy(checkedId: Int) {
        // mengirim id radio button ke View Model untuk mengurutkan data berdasarkan keterangan radio button
        viewModel.getSorted(checkedId)
        // mengambil data countries dari View Model
        viewModel.getCountries().observe(this@MainActivity) { items ->
            if (items != null) {
                // mengupdate adapter untuk recyclerview
                mainAdapter.updateCountries(items)
            }
        }
    }

    private fun initSearch(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // dijalankan saat tombol submit ditekan
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                showData(query)
                searchView.clearFocus() // keluar dari keyboard saat tombol enter di pencet
                return true
            }
            // dijalankan saat perubahan query/text yang dimasukan
            override fun onQueryTextChange(newText: String?): Boolean = runBlocking{
                showLoading(true)
                launch {
                    delay(170)
                    showData(newText)
                }
                return@runBlocking true
            }
        })
    }

    private fun showData(query: String?) = runBlocking{
        launch {
            val formatNumber = DecimalFormat("#,###")
            // memanggil data dari View Model dengan query pencarian
            viewModel.setSummary(query)
            delay(600)
            // mengambil data world dari View Model
            viewModel.getWorld().observe(this@MainActivity) { items ->
                if (items != null) {
                    // set view
                    with(binding) {
                        mainTvWorldRecovered.text =
                            formatNumber.format(items.TotalRecovered?.toFloat())
                        mainTvWorldPositive.text =
                            formatNumber.format(items.TotalConfirmed?.toFloat())
                        mainTvWorldDeaths.text = formatNumber.format(items.TotalDeaths?.toFloat())
                        mainTvDate.text = items.Date
                        dateWorld = items.Date.toString()
                    }
                }
            }
            // mengambil data countries dari View Model
            viewModel.getCountries().observe(this@MainActivity) { items ->
                if (items != null) {
                    // mengupdate adapter untuk recyclerview
                    mainAdapter.updateCountries(items)
                }
                showLoading(false)
            }
        }
    }

    private fun selectedItem(data: Countries) {
        val intent = Intent(this, DetailCountry::class.java)
        val formatNumber = DecimalFormat("#,###")
        // membuat Intent dengan mengirim data
        intent.putExtra(DetailCountry.EXTRA_SLUG, data.Slug)
        intent.putExtra(DetailCountry.EXTRA_COUNTRY_CODE, data.CountryCode)
        intent.putExtra(DetailCountry.EXTRA_COUNTRY, data.Country)
        intent.putExtra(DetailCountry.EXTRA_CONFIRMED, formatNumber.format(data.TotalConfirmed?.toFloat()))
        intent.putExtra(DetailCountry.EXTRA_NEW_CONFIRMED, formatNumber.format(data.NewConfirmed?.toFloat()))
        intent.putExtra(DetailCountry.EXTRA_RECOVERED, formatNumber.format(data.TotalRecovered?.toFloat()))
        intent.putExtra(DetailCountry.EXTRA_NEW_RECOVERED, formatNumber.format(data.NewRecovered?.toFloat()))
        intent.putExtra(DetailCountry.EXTRA_DEATHS, formatNumber.format(data.TotalDeaths?.toFloat()))
        intent.putExtra(DetailCountry.EXTRA_NEW_DEATHS, formatNumber.format(data.NewDeaths?.toFloat()))
        intent.putExtra(DetailCountry.EXTRA_DATE, dateWorld)
        // menjalankan inten
        startActivity(intent)
    }

    private fun showLoading(status: Boolean) {
            if (status) {
                // loading terlihat
                binding.mainProgress.visibility = View.VISIBLE
            } else {
                // loaging menghilang
                binding.mainProgress.visibility = View.GONE
            }
    }

    companion object{
        // fungsi untuk mengubah tema
        fun changeTheme(status: Boolean): Boolean {
            if (status){
                // untuk tema gelap
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                // untuk tema terang
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            return true
        }
    }

}