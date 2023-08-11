package id.budi.agil.covid19.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.budi.agil.covid19.model.DataCase
import id.budi.agil.covid19.model.TableDataCase

@Dao
interface DataCaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dataCase: DataCase)

    @Update
    fun update(dataCase: DataCase)

    @Delete
    fun delete(dataCase: DataCase)

    @Query("SELECT * from datacasecovid WHERE countryCode LIKE :countryCode ORDER BY id ASC")
    fun getCountryDataCase(countryCode: String): LiveData<List<DataCase>>

    @Query("SELECT count(*) from datacasecovid")
    fun dbNotice(): Int
}

@Dao
interface TableCaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tableDataCase: TableDataCase)

    @Update
    fun update(tableDataCase: TableDataCase)

    @Delete
    fun delete(tableDataCase: TableDataCase)

    @Query("SELECT * from tablecasecovid ORDER BY country ASC")
    fun getAllTableDataCase(): LiveData<List<TableDataCase>>

    @Query("SELECT count(*) from tablecasecovid")
    fun dbNotice(): Int
}