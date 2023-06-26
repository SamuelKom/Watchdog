package at.ac.fhcampuswien.watchdog.database

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.ac.fhcampuswien.watchdog.models.LibraryItem
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable

@Database(
    entities = [LibraryItem::class],
    version = 9,
    exportSchema = false
)

@TypeConverters(DatabaseConverters::class)
abstract class LibraryDatabase: RoomDatabase() {

    abstract fun watchableDao(): LibraryDao

    companion object {
        @Volatile
        private var instances = mutableMapOf<String, LibraryDatabase?>()

        fun getDatabase(context: Context, userId: String): LibraryDatabase {
            return instances[userId] ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, LibraryDatabase::class.java, "library")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instances[userId] = it
                    }
            }
        }
    }
}