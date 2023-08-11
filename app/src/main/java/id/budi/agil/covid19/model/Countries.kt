package id.budi.agil.covid19.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Countries(
        // data array dengan object menurut respond dari api
    val Country:String?,
    val Date:String?,
    val NewConfirmed:String?,
    val TotalConfirmed:String?,
    val NewDeaths:String?,
    val TotalDeaths:String?,
    val NewRecovered:String?,
    val TotalRecovered:String?,
    val CountryCode:String?,
    val Slug:String?
): Parcelable
