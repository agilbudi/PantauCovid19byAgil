package id.budi.agil.covid19.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class World(
        // data object menurut respond dari api
    val TotalConfirmed: String?,
    val TotalRecovered: String?,
    val TotalDeaths: String?,
    val Date: String?
): Parcelable{
    constructor(): this("","","", "")
}
