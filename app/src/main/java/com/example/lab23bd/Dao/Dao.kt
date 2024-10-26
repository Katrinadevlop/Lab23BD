package com.example.lab23bd.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab23bd.Db.BoardDbEntity
import com.example.lab23bd.Db.DominoesDbEntity
import com.example.lab23bd.Db.PlayerDbEntity
import com.example.lab23bd.Tuple.BoardInfoTuple
import com.example.lab23bd.Tuple.DominoesInfoTuple
import com.example.lab23bd.Tuple.PlayersInfoTuple

@Dao
interface BoardDao {
    @Insert(entity = BoardDbEntity::class)
    fun insertNewBoardData(board: BoardDbEntity)

    @Insert
    fun insertNewPlayer(player: PlayerDbEntity)

    @Insert
    fun insertNewDomino(domino: DominoesDbEntity)

    @Query("SELECT board.id, dominoes_board, " +
            "players.name_player, players.score_player, \n" +
            "dominoes.left_value, dominoes.right_value FROM board\n" +
            "INNER JOIN players ON board.id = players.id\n" +
            "INNER JOIN dominoes ON board.id = dominoes.id;")
    fun getAllBoardData(): List<BoardInfoTuple>

    @Query("SELECT players.id, players.name_player, players.score_player FROM players\n")
    fun getAllPlayersData(): List<PlayersInfoTuple>

    @Query("SELECT dominoes.id, dominoes.left_value, dominoes.right_value FROM dominoes\n")
    fun getAllDominoesData(): List<DominoesInfoTuple>

    @Query("DELETE FROM board WHERE id = :boardId")
    fun deleteBoardDataById(boardId: Long)

    @Query("DELETE FROM players WHERE id = :playerId")
    fun deletePlayerDataById(playerId: Long)

    @Query("DELETE FROM dominoes WHERE id = :dominoId")
    fun deleteDominoDataById(dominoId: Long)
}