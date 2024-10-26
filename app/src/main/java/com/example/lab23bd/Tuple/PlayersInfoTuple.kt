package com.example.lab23bd.Tuple

import androidx.room.ColumnInfo

data class PlayersInfoTuple (
    val id: Int,
    @ColumnInfo(name = "name_player") val namePlayer: String,
    @ColumnInfo(name = "score_player") val scorePlayer: Int
)