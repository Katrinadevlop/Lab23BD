package com.example.lab23bd.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab23bd.data.database.BoardDbEntity
import com.example.lab23bd.data.database.DominoesDbEntity
import com.example.lab23bd.data.database.PlayerDbEntity
import com.example.lab23bd.domain.model.BoardInfoModel
import com.example.lab23bd.domain.model.DominoesInfoModel
import com.example.lab23bd.domain.model.PlayersInfoModel

@Dao
interface BoardDao {
    @Insert(entity = BoardDbEntity::class)
    fun insertNewBoardData(board: BoardDbEntity)

    @Insert
    fun insertNewPlayer(player: PlayerDbEntity)

    @Insert
    fun insertNewDomino(domino: DominoesDbEntity)

    @Query("SELECT Board.id, Board.dominoes_board,\n" +
            "               Players.name_player, Players.score_player,\n" +
            "               Dominoes.left_value, Dominoes.right_value\n" +
            "        FROM Board\n" +
            "        INNER JOIN Players ON Board.players_id = Players.id\n" +
            "        INNER JOIN Dominoes ON Board.dominoes_id = Dominoes.id")
    fun getAllBoardData(): List<BoardInfoModel>

    @Query("SELECT id, name_player, score_player FROM Players")
    fun getAllPlayersData(): List<PlayersInfoModel>

    @Query("SELECT id, left_value, right_value FROM Dominoes")
    fun getAllDominoesData(): List<DominoesInfoModel>

    @Query("DELETE FROM Board WHERE id = :boardId")
    fun deleteBoardDataById(boardId: Long)

    @Query("DELETE FROM Players WHERE id = :playerId")
    fun deletePlayerDataById(playerId: Long)

    @Query("DELETE FROM Dominoes WHERE id = :dominoId")
    fun deleteDominoDataById(dominoId: Long)
}