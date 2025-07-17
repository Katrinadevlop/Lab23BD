package com.example.lab23bd.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lab23bd.R
import com.example.lab23bd.data.dao.DaoRepository
import com.example.lab23bd.data.database.DatabaseModule
import com.example.lab23bd.data.database.PlayerDbEntity
import kotlinx.coroutines.launch

class PlayersActivity : AppCompatActivity() {
    private lateinit var repository: DaoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseModule.init(applicationContext)
        setContentView(R.layout.activity_players)

        repository = DaoRepository(DatabaseModule.getDatabase().getBoardDao())
        loadPlayersData()
    }

    fun addClick(view: View) {
        val idEditText = findViewById<EditText>(R.id.playerId)
        val nameEditText = findViewById<EditText>(R.id.playerName)
        val scoreEditText = findViewById<EditText>(R.id.playerScore)

        val idText = idEditText.text.toString().trim()
        val nameText = nameEditText.text.toString().trim()
        val scoreText = scoreEditText.text.toString().trim()

        if (idText.isBlank() || nameText.isBlank() || scoreText.isBlank()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_LONG).show()
            return
        }

        val id = idText.toIntOrNull()
        val score = scoreText.toIntOrNull()

        if (id == null || id <= 0) {
            Toast.makeText(this, "ID должен быть положительным числом", Toast.LENGTH_LONG).show()
            return
        }

        if (nameText.length < 2) {
            Toast.makeText(this, "Имя должно содержать минимум 2 символа", Toast.LENGTH_LONG).show()
            return
        }

        if (score == null || score < 0) {
            Toast.makeText(this, "Очки должны быть неотрицательным числом", Toast.LENGTH_LONG).show()
            return
        }

        val player = PlayerDbEntity(
            id = id,
            namePlayer = nameText,
            scorePlayer = score.toString()
        )

        lifecycleScope.launch {
            try {
                repository.insertNewPlayer(player)
                Toast.makeText(this@PlayersActivity, "Игрок добавлен", Toast.LENGTH_SHORT).show()
                loadPlayersData()
                clearFields()
            } catch (e: Exception) {
                Toast.makeText(this@PlayersActivity, "Ошибка добавления: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearFields() {
        findViewById<EditText>(R.id.playerId).text.clear()
        findViewById<EditText>(R.id.playerName).text.clear()
        findViewById<EditText>(R.id.playerScore).text.clear()
    }

    private fun loadPlayersData() {
        lifecycleScope.launch {
            try {
                val playerData = repository.getAllPlayersData()
                val adapter = ArrayAdapter(
                    this@PlayersActivity,
                    android.R.layout.simple_list_item_1,
                    playerData.map { "ID: ${it.id}, Имя: ${it.namePlayer}, Очки: ${it.scorePlayer}" }
                )
                findViewById<GridView>(R.id.gridView).adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@PlayersActivity, "Ошибка загрузки данных: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openBoardTableClick(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun openDominoesTableClick(view: View) {
        startActivity(Intent(this, DominoesActivity::class.java))
    }
}
