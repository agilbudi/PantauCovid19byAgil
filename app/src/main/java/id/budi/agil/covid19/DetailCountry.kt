package id.budi.agil.covid19

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.budi.agil.covid19.databinding.ActivityDetailCountryBinding
import id.budi.agil.covid19.view_model.DetailViewModel
import java.util.*

class DetailCountry : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCountryBinding
    private lateinit var viewModel: DetailViewModel
    companion object{
        const val EXTRA_SLUG = "slug"
        const val EXTRA_COUNTRY_CODE = "country_code"
        const val EXTRA_COUNTRY = "country"
        const val EXTRA_NEW_CONFIRMED = "new_confirmed"
        const val EXTRA_NEW_DEATHS = "new_deaths"
        const val EXTRA_NEW_RECOVERED = "new_recovered"
        const val EXTRA_CONFIRMED = "confirmed"
        const val EXTRA_DEATHS = "deaths"
        const val EXTRA_RECOVERED = "recovered"
        const val EXTRA_DATE = "date"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slug = intent.getStringExtra(EXTRA_SLUG)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        binding.detailFabMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.detailFabMenu)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.menu_theme_light -> MainActivity.changeTheme(false)
                    R.id.menu_theme_night -> MainActivity.changeTheme(true)
                    else -> MainActivity.changeTheme(false)
                }
            }
            popupMenu.show()
        }

        detailShow()
        if (slug != null) chartShow(slug)
    }

    private fun chartShow(slug: String) {
        val chart = binding.detailLineChart
        showLoading(2, true)
        viewModel.setDataCases(slug)
        viewModel.getDataCases().observe(this, {data ->
            if (data != null){
                val lineConfirmed = LineDataSet(data.dataConfirmed, resources.getString(R.string.confirmed))
                val lineRecovered = LineDataSet(data.dataRecovered, resources.getString(R.string.recovered))
                val lineActive = LineDataSet(data.dataActive, resources.getString(R.string.positive))
                val lineDeaths = LineDataSet(data.dataDeaths, resources.getString(R.string.deaths))

                setupDrawing(lineConfirmed, "537388")
                setupDrawing(lineRecovered, "00C853")
                setupDrawing(lineActive, "FFAB00")
                setupDrawing(lineDeaths, "D50000")

                val xAxis: XAxis = chart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(data.dateCases)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.setCenterAxisLabels(false)
                xAxis.isGranularityEnabled = true

                val lineData = LineData(lineConfirmed, lineRecovered, lineActive, lineDeaths)
                chart.data = lineData
                chart.axisLeft.axisMinimum = 0f
                chart.axisRight.isEnabled = false
                chart.xAxis.labelRotationAngle = 0f
                chart.setTouchEnabled(true)
                chart.setPinchZoom(true)
                chart.description.isEnabled = false
                chart.setNoDataText(resources.getString(R.string.nodata))
                chart.setNoDataTextColor(R.attr.colorOnSecondary)
                chart.animateX(1900, Easing.EaseInOutElastic)
                chart.invalidate()
                chart.setVisibleXRangeMaximum(30f)
            }
        })
        showLoading(2, false)
    }

    private fun setupDrawing(line: LineDataSet, parseColor: String) {
        line.setDrawValues(false)
        line.setDrawFilled(true)
        line.lineWidth = 2f
        line.color = Color.parseColor("#$parseColor")
        line.fillColor = Color.parseColor("#D0$parseColor")
    }

    private fun detailShow() {
        showLoading(1,true)
        val country = intent.getStringExtra(EXTRA_COUNTRY)
        val countryCode = intent.getStringExtra(EXTRA_COUNTRY_CODE)
        val urlFlagCountry = "https://www.countryflags.io/${countryCode?.toLowerCase(Locale.ROOT)}/shiny/64.png"
        val totalConfirmed = intent.getStringExtra(EXTRA_CONFIRMED)
        val totalRecovered = intent.getStringExtra(EXTRA_RECOVERED)
        val totalDeaths = intent.getStringExtra(EXTRA_DEATHS)
        val newConfirmed = intent.getStringExtra(EXTRA_NEW_CONFIRMED)
        val newRecovered = intent.getStringExtra(EXTRA_NEW_RECOVERED)
        val newDeaths = intent.getStringExtra(EXTRA_NEW_DEATHS)
        val date = intent.getStringExtra(EXTRA_DATE)

        with(binding){
            Glide.with(this@DetailCountry)
                .load(urlFlagCountry)
                .apply(RequestOptions().override(200,84))
                .apply(RequestOptions().centerCrop())
                .into(detailIcFlag)
            detailTvCountry.text = country
            detailTvTotalConfirmed.text = totalConfirmed
            detailTvTotalRecovered.text = totalRecovered
            detailTvTotalDeaths.text = totalDeaths
            detailTvConfirmed.text = newConfirmed
            detailTvRecovered.text = newRecovered
            detailTvDeaths.text = newDeaths
            detailTvDate.text = date
        }
        showLoading(1,false)
    }

    private fun showLoading(select: Int,status: Boolean){
        when(select){
            1-> if (status){
                binding.detailProgress1.visibility = View.VISIBLE
            }else{
                binding.detailProgress1.visibility = View.GONE
            }
            2-> if (status){
                binding.detailProgress2.visibility = View.VISIBLE
            }
            else{
                binding.detailProgress2.visibility = View.GONE
            }
        }
    }
}