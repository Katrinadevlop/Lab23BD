package com.example.lab23bd.Db

import android.content.Context
import androidx.room.Room

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun getDatabase(): AppDatabase {
        return appDatabase
    }
}
