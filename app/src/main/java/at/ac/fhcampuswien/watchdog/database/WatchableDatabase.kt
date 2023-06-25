package at.ac.fhcampuswien.watchdog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.ac.fhcampuswien.watchdog.models.LibraryItem
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
        private var Instance: WatchableDatabase? = null

        fun getDatabase(context: Context): WatchableDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WatchableDatabase::class.java, "watchable_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}