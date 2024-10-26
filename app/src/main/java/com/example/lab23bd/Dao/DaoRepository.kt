package com.example.lab23bd.Dao

import com.example.lab23bd.Db.BoardDbEntity
import com.example.lab23bd.Db.DominoesDbEntity
import com.example.lab23bd.Db.PlayerDbEntity
import com.example.lab23bd.Tuple.BoardInfoTuple
import com.example.lab23bd.Tuple.DominoesInfoTuple
import com.example.lab23bd.Tuple.PlayersInfoTuple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DaoRepository (private val BoardDao: BoardDao) {

    suspend fun insertNewBoardData(statisticDbEntity: BoardDbEntity) {
        withContext(Dispatchers.IO) {
            BoardDao.insertNewBoardData(statisticDbEntity)
        }
    }

    suspend fun getAllBoardData(): List<BoardInfoTuple> {
        return withContext(Dispatchers.IO) {
            return@withContext BoardDao.getAllBoardData()
        }
    }

    suspend fun removeBoardDataById(id: Long) {
        withContext(Dispatchers.IO) {
            BoardDao.deleteBoardDataById(id)
        }
    }

    suspend fun insertNewPlayer(player: PlayerDbEntity) {
        withContext(Dispatchers.IO) {
            BoardDao.insertNewPlayer(player)
        }
    }

    suspend fun getAllPlayersData(): List<PlayersInfoTuple> {
        return withContext(Dispatchers.IO) {
            return@withContext BoardDao.getAllPlayersData()
        }
    }

    suspend fun removePlayerDataById(id: Long) {
        withContext(Dispatchers.IO) {
            BoardDao.deletePlayerDataById(id)
        }
    }

    suspend fun insertNewDomino(domino: DominoesDbEntity) {
        withContext(Dispatchers.IO) {
            BoardDao.insertNewDomino(domino)
        }
    }


    suspend fun getAllDominoData(): List<DominoesInfoTuple> {
        return withContext(Dispatchers.IO) {
            return@withContext BoardDao.getAllDominoesData()
        }
    }

    suspend fun removeDominoDataById(id: Long) {
        withContext(Dispatchers.IO) {
            BoardDao.deleteDominoDataById(id)
        }
    }

}