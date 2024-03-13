package io.github.bikkysamuel.animetv.app

import android.app.Application
import androidx.room.Room
import io.github.bikkysamuel.animetv.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        roomDb = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "anime_tv"
        ).build()
    }

    companion object {
        lateinit var instance: MyApp
            private set

        lateinit var roomDb: AppDatabase
    }
}