package com.example.lab23bd.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab23bd.data.dao.BoardDao

@Database(
    version = 2,
    entities = [
        PlayerDbEntity::class,
        DominoesDbEntity::class,
        BoardDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getBoardDao(): BoardDao

}
