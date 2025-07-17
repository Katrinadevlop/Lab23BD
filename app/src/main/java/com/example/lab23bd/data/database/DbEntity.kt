package com.example.lab23bd.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name_player") val namePlayer:String,
    @ColumnInfo(name = "score_player") val scorePlayer: String,
)

@Entity(tableName = "dominoes")
data class DominoesDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "left_value") val leftValue:Int,
    @ColumnInfo(name = "right_value") val rightValue:Int,
)

@Entity(
    tableName = "board",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = PlayerDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["players_id"]
        ),
        ForeignKey(
            entity = DominoesDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["dominoes_id"]
        )
    ]
)

data class BoardDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "players_id") val playersId: Int,
    @ColumnInfo(name = "dominoes_id") val dominoesId: Int,
    @ColumnInfo(name = "dominoes_board") val dominoesBoard:String,
)
