package id.budi.agil.covid19.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataCase(
    val dateReported: String,
    val countryCode: String,
    val country: String,
    val newCase: Int,
    val totalCase: Int,
    val newDeath: Int,
    val totalDeath: Int
): Parcelable
