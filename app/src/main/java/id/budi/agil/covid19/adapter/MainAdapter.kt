package id.budi.agil.covid19.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.budi.agil.covid19.databinding.ItemListBinding
import id.budi.agil.covid19.model.Countries
import id.budi.agil.covid19.model.World
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter: RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    private val countriesList = ArrayList<Countries>()
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: Countries)
    }

    inner class ListViewHolder(private val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(countries: Countries){
            val urlFlagCountry = "https://www.countryflags.io/${countries.CountryCode?.toLowerCase(Locale.ROOT)}/shiny/64.png"
            with(binding){
                Glide.with(itemView.context)
                    .load(urlFlagCountry)
                    .apply(RequestOptions().override(90,54))
                    .apply(RequestOptions().centerCrop())
                    .into(itemIcFlag)

                itemTvCountry.text = countries.Country
                itemTvRecovered.text = countries.TotalRecovered
                itemTvPositive.text = countries.TotalConfirmed
                itemTvDeath.text = countries.TotalDeaths

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(countries) }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun updateCountries(newCountries:ArrayList<Countries>){
        countriesList.clear()
        countriesList.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(countriesList[position])
    }

    override fun getItemCount(): Int = countriesList.size
}