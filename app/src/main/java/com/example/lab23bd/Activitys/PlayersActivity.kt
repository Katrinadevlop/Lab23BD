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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db").build()
        val boardDao = Dependencies.getDatabase().getBoardDao()
        repository = DaoRepository(boardDao)

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
            val boardDao = Dependencies.getDatabase().getBoardDao()
            val repository = DaoRepository(boardDao)

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
            val boardDao = Dependencies.getDatabase().getBoardDao()
            val repository = DaoRepository(boardDao)

            val playerData = repository.getAllPlayersData()
            val adapter = ArrayAdapter(
                this@PlayersActivity,
                android.R.layout.simple_list_item_1,
                playerData.map { "ID: ${it.id}, Имя: ${it.namePlayer}, Очки: ${it.scorePlayer}" }
            )
            findViewById<GridView>(R.id._dynamic).adapter = adapter
        }
    }

    fun openBoardTableClick(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun openDominoesTableClick(view: View){
        val intent = Intent(this, DominosActivity::class.java)
        startActivity(intent)
    }
}