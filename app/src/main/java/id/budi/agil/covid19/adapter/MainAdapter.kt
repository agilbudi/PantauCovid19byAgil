package id.budi.agil.covid19.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.budi.agil.covid19.MainActivity
import id.budi.agil.covid19.databinding.ItemListBinding
import id.budi.agil.covid19.model.Countries
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter: RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    private val countriesList = ArrayList<Countries>()
    private var onItemClickCallback: OnItemClickCallback? = null

    // interface untuk item yang diklik
    interface OnItemClickCallback {
        fun onItemClicked(data: Countries)
    }

    inner class ListViewHolder(private val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(countries: Countries){
            // membuat url untuk gambar bendera sesuai CountryCode negara
            val urlFlagCountry = "https://www.countryflags.io/${countries.CountryCode?.toLowerCase(Locale.ROOT)}/shiny/64.png"
            val formatNumber = DecimalFormat("#,###")
            with(binding){
                // set view
                Glide.with(itemView.context)
                    .load(urlFlagCountry)
                    .apply(RequestOptions().override(90,54))
                    .apply(RequestOptions().centerCrop())
                    .into(itemIcFlag)

                itemTvCountry.text = countries.Country
                itemTvRecovered.text = formatNumber.format(countries.TotalRecovered?.toFloat())
                itemTvPositive.text = formatNumber.format(countries.TotalConfirmed?.toFloat())
                itemTvDeath.text = formatNumber.format(countries.TotalDeaths?.toFloat())

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(countries) } // memberi event klik, pada item disini
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        // saat item diklik
        this.onItemClickCallback = onItemClickCallback
    }

    fun updateCountries(newCountries:ArrayList<Countries>){
        // mengupdate data countriesList
        countriesList.clear()
        countriesList.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        // menentukan layout item yang digunakan
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        // menentukan tiap item
        holder.bind(countriesList[position])
    }

    // jumlah item yang ada
    override fun getItemCount(): Int = countriesList.size
}