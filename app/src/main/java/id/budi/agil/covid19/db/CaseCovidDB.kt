package id.budi.agil.covid19.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DataCaseCovid::class, TableCaseCovid::class], version = 1)
abstract class CaseCovidDB : RoomDatabase() {
    abstract fun dataCaseDao(): DataCaseDao
    abstract fun tableCaseDao(): TableCaseDao

    companion object{
        @Volatile
        private var INSTANCE: CaseCovidDB? = null

        @JvmStatic
        fun getDatabase (context: Context): CaseCovidDB {
            if(INSTANCE == null){
                synchronized(CaseCovidDB::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    CaseCovidDB::class.java, "covid_database")
                        .build()
                }
            }
            return INSTANCE as CaseCovidDB
        }
    }
}