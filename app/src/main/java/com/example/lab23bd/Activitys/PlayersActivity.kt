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
import androidx.lifecycle.lifecycleScope
import com.example.lab23bd.Dao.DaoRepository
import com.example.lab23bd.Db.Dependencies
import com.example.lab23bd.Db.PlayerDbEntity
import com.example.lab23bd.R
import kotlinx.coroutines.launch

class PlayersActivity : AppCompatActivity() {
    private lateinit var repository: DaoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.init(applicationContext)
        enableEdgeToEdge()
        setContentView(R.layout.activity_players)

        repository = DaoRepository(Dependencies.getDatabase().getBoardDao())
        loadPlayersData()
    }

    fun addClick(view: View) {
        val idEditText = findViewById<EditText>(R.id.playerId)
        val nameEditText = findViewById<EditText>(R.id.playerName)
        val scoreEditText = findViewById<EditText>(R.id.playerScore)

        if (idEditText.text.isEmpty() || nameEditText.text.isEmpty() || scoreEditText.text.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            return
        }

        val player = PlayerDbEntity(
            id = idEditText.text.toString().toInt(),
            namePlayer = nameEditText.text.toString(),
            scorePlayer = scoreEditText.text.toString()
        )

        lifecycleScope.launch {
            try {
                repository.insertNewPlayer(player)
                Toast.makeText(this@PlayersActivity, "Игрок добавлен", Toast.LENGTH_SHORT).show()
                loadPlayersData()
            } catch (e: Exception) {
                Toast.makeText(this@PlayersActivity, "Ошибка добавления: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
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
                findViewById<GridView>(R.id._dynamic).adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@PlayersActivity, "Ошибка загрузки данных: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openBoardTableClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun openDominoesTableClick(view: View) {
        val intent = Intent(this, DominosActivity::class.java)
        startActivity(intent)
    }
}
