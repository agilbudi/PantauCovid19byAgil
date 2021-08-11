package id.budi.agil.covid19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.budi.agil.covid19.adapter.MainAdapter
import id.budi.agil.covid19.databinding.ActivityMainBinding
import id.budi.agil.covid19.model.Countries
import id.budi.agil.covid19.view_model.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val mainAdapter = MainAdapter()
    private lateinit var dateWorld: String
    companion object{
        fun changeTheme(status: Boolean): Boolean {
            if (status){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchView = binding.mainSv
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.mainRvCountries.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
        binding.mainFabMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.mainFabMenu)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.menu_theme_light -> changeTheme(false)
                    R.id.menu_theme_night -> changeTheme(true)
                    else -> changeTheme(false)
                }
            }
            popupMenu.show()
        }
        showLoading(true)
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                showData(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = runBlocking{
                showLoading(true)
                launch {
                    delay(170)
                    showData(newText)
                }
                return@runBlocking true
            }
        })
        if(viewModel.getCountries().value == null){
            showLoading(true)
            showData(null)
        }

        mainAdapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Countries) {
                selectedItem(data)
            }
        })

    }

    private fun showData(query: String?) {
        viewModel.setSummary(query)
        viewModel.getWorld().observe(this, { items->
            if (items != null){
                with(binding){
                    mainTvWorldRecovered.text = items.TotalRecovered
                    mainTvWorldPositive.text = items.TotalConfirmed
                    mainTvWorldDeaths.text = items.TotalDeaths
                    mainTvDate.text = items.Date
                    dateWorld = items.Date.toString()
                }
            }
        })
        viewModel.getCountries().observe(this, { items ->
            if (items != null) {
                mainAdapter.updateCountries(items)
            }
            showLoading(false)
        })
    }

    private fun selectedItem(data: Countries) {
        val intent = Intent(this, DetailCountry::class.java)
        intent.putExtra(DetailCountry.EXTRA_SLUG, data.Slug)
        intent.putExtra(DetailCountry.EXTRA_COUNTRY_CODE, data.CountryCode)
        intent.putExtra(DetailCountry.EXTRA_COUNTRY, data.Country)
        intent.putExtra(DetailCountry.EXTRA_CONFIRMED, data.TotalConfirmed)
        intent.putExtra(DetailCountry.EXTRA_NEW_CONFIRMED, data.NewConfirmed)
        intent.putExtra(DetailCountry.EXTRA_RECOVERED, data.TotalRecovered)
        intent.putExtra(DetailCountry.EXTRA_NEW_RECOVERED, data.NewRecovered)
        intent.putExtra(DetailCountry.EXTRA_DEATHS, data.TotalDeaths)
        intent.putExtra(DetailCountry.EXTRA_NEW_DEATHS, data.NewDeaths)
        intent.putExtra(DetailCountry.EXTRA_DATE, dateWorld)
        startActivity(intent)
        Toast.makeText(this, "You are clicked ${data.Country}.", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(status: Boolean){
        if(status){
            binding.mainProgress.visibility = View.VISIBLE
        }else{
            binding.mainProgress.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}