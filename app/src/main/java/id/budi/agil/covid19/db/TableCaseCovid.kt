package id.budi.agil.covid19.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TableCaseCovid(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "countryCode")
    var countryCode: String? = null,

    @ColumnInfo(name = "country")
    var country: String? = null,

    @ColumnInfo(name = "region")
    var region: String? = null,

    @ColumnInfo(name = "totalCase")
    var totalCase: Int = 0,

    @ColumnInfo(name = "weeklyCase")
    var weeklyCase: Int = 0,

    @ColumnInfo(name = "totalDeath")
    var totalDeath: Int = 0,

    @ColumnInfo(name = "weeklyDeath")
    var weeklyDeath: Int =0
): Parcelable
