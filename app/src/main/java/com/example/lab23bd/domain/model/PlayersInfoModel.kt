package com.example.lab23bd.domain.model

import androidx.room.ColumnInfo

data class PlayersInfoModel (
    val id: Int,
    @ColumnInfo(name = "name_player") val namePlayer: String,
    @ColumnInfo(name = "score_player") val scorePlayer: String
)