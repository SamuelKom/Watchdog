package at.ac.fhcampuswien.watchdog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.ac.fhcampuswien.watchdog.models.LibraryItem

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
        private var Instance: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LibraryDatabase::class.java, "library_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}