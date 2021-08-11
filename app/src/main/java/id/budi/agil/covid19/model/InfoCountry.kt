package id.budi.agil.covid19.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InfoCountry(
    val Deaths:String?,
    val Confirmed:String?,
    val Recovered:String?,
    val Active:String?,
    val Date:String?
):Parcelable{
    constructor(): this("","","",
    "","")
}
