package myapplication.WorkoutApp

import android.app.Application

class WorkOutApp: Application() {

    var db: HistoryDatabase? = null

    fun getDatabase(): HistoryDatabase {
        if (db == null) {
            db = HistoryDatabase.getInstance(this)
        }
        return db!!
    }

    override fun onCreate() {
        super.onCreate()
        db = HistoryDatabase.getInstance(this)
    }
}