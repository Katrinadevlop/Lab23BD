package com.example.lab23bd.Activitys

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
import com.example.lab23bd.Db.AppDatabase
import com.example.lab23bd.Dao.DaoRepository
import com.example.lab23bd.Db.BoardDbEntity
import com.example.lab23bd.Db.Dependencies
import com.example.lab23bd.Db.DominoesDbEntity
import com.example.lab23bd.Db.PlayerDbEntity
import com.example.lab23bd.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var repository: DaoRepository
    private lateinit var boardNameEditText: EditText
    private lateinit var playerNameEditText: EditText
    private lateinit var scoreEditText: EditText
    private lateinit var leftValueEditText: EditText
    private lateinit var rightValueEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.init(applicationContext)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        boardNameEditText = findViewById(R.id.boardName)
        playerNameEditText = findViewById(R.id.playerName)
        scoreEditText = findViewById(R.id.score)
        leftValueEditText = findViewById(R.id.leftValue)
        rightValueEditText = findViewById(R.id.rightValue)

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
        val gridView = findViewById<GridView>(R.id._dynamic)
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
        val intent = Intent(this, DominosActivity::class.java)
        startActivity(intent)
    }

    fun addClicks(view: View) {
        lifecycleScope.launch {
            val playerName = playerNameEditText.text.toString()
            val score = scoreEditText.text.toString()
            val leftValue = leftValueEditText.text.toString()
            val rightValue = rightValueEditText.text.toString()
            val boardName = boardNameEditText.text.toString()

            if (playerName.isBlank() || score.isBlank() || leftValue.isBlank() || rightValue.isBlank() || boardName.isBlank()) {
                Toast.makeText(this@MainActivity, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }

            val player = PlayerDbEntity(0, playerName, score)
            repository.insertNewPlayer(player)

            val domino = DominoesDbEntity(0, leftValue.toInt(), rightValue.toInt())
            repository.insertNewDomino(domino)

            val lastPlayerId = repository.getAllPlayersData().last().id
            val lastDominoId = repository.getAllDominoData().last().id

            val boardEntry = BoardDbEntity(0, lastPlayerId, lastDominoId, boardName)
            repository.insertNewBoardData(boardEntry)

            loadBoardData()

            clearInputFields()
        }
    }

    private fun clearInputFields() {
        boardNameEditText.text.clear()
        playerNameEditText.text.clear()
        scoreEditText.text.clear()
        leftValueEditText.text.clear()
        rightValueEditText.text.clear()
    }
}
