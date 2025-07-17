package com.example.lab23bd.domain.model

import androidx.room.ColumnInfo

data class DominoesInfoModel (
    val id:Int,
    @ColumnInfo(name = "left_value") val leftValue: Int,
    @ColumnInfo(name = "right_value") val rightValue: Int
)