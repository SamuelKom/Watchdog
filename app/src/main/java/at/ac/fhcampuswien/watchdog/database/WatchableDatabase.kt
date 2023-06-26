package at.ac.fhcampuswien.watchdog.database

import android.app.Activity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable

@Database(
    entities = [Watchable::class, Movie::class, Series::class],
    version = 7,
    exportSchema = false
)

@TypeConverters(DatabaseConverters::class)
abstract class WatchableDatabase: RoomDatabase() {

    abstract fun watchableDao(): WatchableDao

    companion object {
        @Volatile
        private var instances = mutableMapOf<String, WatchableDatabase?>()

        fun getDatabase(activity: Activity, userId: String): WatchableDatabase {
            println(instances[userId])
            val context = activity.baseContext

            println(instances.size)

            return instances[userId] ?: synchronized(this) {
                Room.databaseBuilder(context, WatchableDatabase::class.java, "watchableDB4_$userId")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instances[userId] = it
                    }
            }
        }
    }
}