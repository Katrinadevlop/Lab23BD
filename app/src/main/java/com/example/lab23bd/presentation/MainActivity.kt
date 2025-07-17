package com.example.lab23bd.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.lab23bd.data.database.AppDatabase
import com.example.lab23bd.data.dao.DaoRepository
import com.example.lab23bd.data.database.BoardDbEntity
import com.example.lab23bd.data.database.DatabaseModule
import com.example.lab23bd.data.database.DominoesDbEntity
import com.example.lab23bd.data.database.PlayerDbEntity
import com.example.lab23bd.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var repository: DaoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseModule.init(applicationContext)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db").build()
        val boardDao = db.getBoardDao()
        repository = DaoRepository(boardDao)

        lifecycleScope.launch {
            loadBoardData()
        }

        Toast.makeText(this, "Заполните все поля для добавления данных", Toast.LENGTH_LONG).show()
    }

    private suspend fun loadBoardData() {
        val gridView = findViewById<GridView>(R.id.gridView)
        val boardData = repository.getAllBoardData()
        val adapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_list_item_1,
            boardData.map {
                "ID: ${it.id}, Доска: ${it.dominoesBoard}, Имя: ${it.namePlayer}, " +
                        "Очки: ${it.scorePlayer}, Правая сторона: ${it.leftValue}, " +
                        "Левая сторона: ${it.rightValue}"
            }
        )
        gridView.adapter = adapter
    }

    fun openPlayersTableClick(view: View) {
        val intent = Intent(this, PlayersActivity::class.java)
        startActivity(intent)
    }

    fun openDominoesTableClick(view: View) {
        val intent = Intent(this, DominoesActivity::class.java)
        startActivity(intent)
    }

    fun addClicks(view: View) {
        lifecycleScope.launch {
            val playerName = findViewById<EditText>(R.id.playerName).text.toString()
            val score = findViewById<EditText>(R.id.score).text.toString()
            val leftValueText = findViewById<EditText>(R.id.leftValue).text.toString()
            val rightValueText = findViewById<EditText>(R.id.rightValue).text.toString()
            val boardName =  findViewById<EditText>(R.id.boardName).text.toString()

            if (playerName.isBlank() || score.isBlank() || leftValueText.isBlank()
                || rightValueText.isBlank() || boardName.isBlank()
            ) {
                Toast.makeText(this@MainActivity, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val leftValue = leftValueText.toIntOrNull()
            val rightValue = rightValueText.toIntOrNull()

            if (leftValue == null || rightValue == null) {
                Toast.makeText(this@MainActivity, "Левая и правая сторона должны быть числами", Toast.LENGTH_SHORT).show()
                return@launch
            }

            try {
                val player = PlayerDbEntity(0, playerName, score)
                repository.insertNewPlayer(player)

                val domino = DominoesDbEntity(0, leftValue, rightValue)
                repository.insertNewDomino(domino)

                val players = repository.getAllPlayersData()
                val dominoes = repository.getAllDominoData()

                if (players.isEmpty() || dominoes.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val lastPlayerId = players.last().id
                val lastDominoId = dominoes.last().id

                val boardEntry = BoardDbEntity(0, lastPlayerId, lastDominoId, boardName)
                repository.insertNewBoardData(boardEntry)

                loadBoardData()
                clearInputFields()
                Toast.makeText(this@MainActivity, "Данные успешно добавлены", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearInputFields() {
        findViewById<EditText>(R.id.idDomino).text.clear()
        findViewById<EditText>(R.id.boardName).text.clear()
        findViewById<EditText>(R.id.playerName).text.clear()
        findViewById<EditText>(R.id.score).text.clear()
        findViewById<EditText>(R.id.leftValue).text.clear()
        findViewById<EditText>(R.id.rightValue).text.clear()
    }
}
