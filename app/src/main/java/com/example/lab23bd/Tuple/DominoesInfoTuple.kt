package com.example.lab23bd.Tuple

import androidx.room.ColumnInfo

data class DominoesInfoTuple (
    val id:Int,
    @ColumnInfo(name = "left_value") val leftValue: Int,
    @ColumnInfo(name = "right_value") val rightValue: Int
)