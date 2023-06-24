package at.ac.fhcampuswien.watchdog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.ac.fhcampuswien.watchdog.models.User

@Database(
    entities = [User::class],
    version = 4,
    exportSchema = false
)
//@TypeConverters(ColorTypeConverter::class)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, UserDatabase::class.java, "user_db")
                    .fallbackToDestructiveMigration()
                    //.addTypeConverter(ColorTypeConverter())
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}