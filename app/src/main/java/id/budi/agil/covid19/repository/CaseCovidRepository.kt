package id.budi.agil.covid19.repository

import android.app.Application
import id.budi.agil.covid19.db.CaseCovidDB
import id.budi.agil.covid19.db.DataCaseDao
import id.budi.agil.covid19.db.TableCaseDao
import id.budi.agil.covid19.model.DataCase
import id.budi.agil.covid19.model.TableDataCase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaseCovidRepository(application: Application) {
    private val mDataCaseDao: DataCaseDao
    private val mTableCaseDao: TableCaseDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = CaseCovidDB.getDatabase(application)
        mDataCaseDao = db.dataCaseDao()
        mTableCaseDao = db.tableCaseDao()
    }

    fun getCountryDataCase(countryCode: String) = mDataCaseDao.getCountryDataCase(countryCode)
    fun getAllTableDataCase() = mTableCaseDao.getAllTableDataCase()

    fun dbEmpty(): Boolean{
        return (mTableCaseDao.dbNotice() == 0 || mDataCaseDao.dbNotice() == 0)
    }

    fun insertDataCase(dataCase: DataCase) = executorService.execute {
        mDataCaseDao.insert(dataCase)
    }
    fun insertTableCase(tableDataCase: TableDataCase) = executorService.execute {
        mTableCaseDao.insert(tableDataCase)
    }

    fun updateDataCase(dataCase: DataCase) = executorService.execute {
        mDataCaseDao.update(dataCase)
    }
    fun updateTableCase(tableDataCase: TableDataCase) = executorService.execute {
        mTableCaseDao.update(tableDataCase)
    }

    fun deleteDataCase(dataCase: DataCase) = executorService.execute {
        mDataCaseDao.delete(dataCase)
    }
    fun deleteTableCase(tableDataCase: TableDataCase) = executorService.execute {
        mTableCaseDao.delete(tableDataCase)
    }
}