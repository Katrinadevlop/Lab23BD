package com.example.lab23bd.Db

import android.content.Context
import androidx.room.Room

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    // Создаем базу данных с помощью Lazy и предоставляем метод для доступа к ней
    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration() // Это позволит удалить старую базу данных, если миграция отсутствует
            .build()
    }

    fun getDatabase(): AppDatabase {
        return appDatabase
    }
}
