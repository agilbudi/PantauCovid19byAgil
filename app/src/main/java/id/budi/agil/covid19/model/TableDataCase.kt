package id.budi.agil.covid19.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TableDataCase(
    val country: String,
    val region: String,
    val totalCase: Int,
    val weeklyCase: Int,
    val totalDeath: Int,
    val weeklyDeath: Int
): Parcelable
