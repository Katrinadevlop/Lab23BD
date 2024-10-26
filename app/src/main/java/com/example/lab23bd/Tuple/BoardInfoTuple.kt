package com.example.lab23bd.Tuple

import androidx.room.ColumnInfo

data class BoardInfoTuple (
    val id: Int,
    @ColumnInfo(name = "dominoes_board") val dominoesBoard:String,
    @ColumnInfo(name = "name_player") val namePlayer: String,
    @ColumnInfo(name = "score_player") val scorePlayer: Int,
    @ColumnInfo(name = "left_value") val leftValue: Int,
    @ColumnInfo(name = "right_value") val rightValue: Int
)
