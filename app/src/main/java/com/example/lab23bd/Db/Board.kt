package com.example.lab23bd.Db

data class Board (
    val dominoesId: Int,
    val playersId:Int,
    val dominoesBoard:String
)
{
    fun toBoardDbEntity(): BoardDbEntity = BoardDbEntity(
        id = 0,
        playersId = playersId,
        dominoesId = dominoesId,
        dominoesBoard = dominoesBoard
    )
}