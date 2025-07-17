package com.example.lab23bd.data.dao

import com.example.lab23bd.data.database.BoardDbEntity
import com.example.lab23bd.data.database.DominoesDbEntity
import com.example.lab23bd.data.database.PlayerDbEntity
import com.example.lab23bd.domain.model.BoardInfoModel
import com.example.lab23bd.domain.model.DominoesInfoModel
import com.example.lab23bd.domain.model.PlayersInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DaoRepository (private val BoardDao: BoardDao) {

    suspend fun insertNewBoardData(statisticDbEntity: BoardDbEntity) {
        withContext(Dispatchers.IO) {
            BoardDao.insertNewBoardData(statisticDbEntity)
        }
    }

    suspend fun getAllBoardData(): List<BoardInfoModel> {
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

    suspend fun getAllPlayersData(): List<PlayersInfoModel> {
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


    suspend fun getAllDominoData(): List<DominoesInfoModel> {
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