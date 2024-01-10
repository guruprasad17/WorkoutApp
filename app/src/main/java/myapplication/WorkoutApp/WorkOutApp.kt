package myapplication.WorkoutApp

import android.app.Application
import io.branch.referral.Branch

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



        Branch.enableLogging()

        // Branch object initialization
        Branch.getAutoInstance(this)
    }
}