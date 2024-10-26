package com.example.lab23bd.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.lab23bd.Db.DominoesDbEntity
import com.example.lab23bd.Db.PlayerDbEntity
import com.example.lab23bd.R
import kotlinx.coroutines.launch

class DominosActivity : AppCompatActivity() {
    private lateinit var repository: DaoRepository
    private lateinit var listView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.init(applicationContext)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dominos)

        listView = findViewById(R.id._dynamic)
        val boardDao = Dependencies.getDatabase().getBoardDao()
        repository = DaoRepository(boardDao)

        loadDominoData()
    }

    private fun loadDominoData() {
        lifecycleScope.launch {
            val dominoData = repository.getAllDominoData()
            val adapter = ArrayAdapter(
                this@DominosActivity,
                android.R.layout.simple_list_item_1,
                dominoData.map { "ID: ${it.id}, Правая сторона: ${it.leftValue}, Левая сторона: ${it.rightValue}" }
            )
            listView.adapter = adapter
        }
    }

    fun addClick(view: View) {
        val idEditText = findViewById<EditText>(R.id.idDomino)
        val leftScore = findViewById<EditText>(R.id.leftScore)
        val rightScore = findViewById<EditText>(R.id.rightScore)

        if (idEditText.text.isEmpty() || leftScore.text.isEmpty() || rightScore.text.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            return
        }

        val dominos = DominoesDbEntity(
            id = idEditText.text.toString().toInt(),
            leftValue = leftScore.text.toString().toInt(),
            rightValue =  rightScore.text.toString().toInt()
        )

        lifecycleScope.launch {
            repository.insertNewDomino(dominos)
            loadDominoData()
            try {
                repository.insertNewDomino(dominos)
                Log.d("DominosActivity", "Запись добавлена: ID: ${dominos.id}, Левый: ${dominos.leftValue}, Правый: ${dominos.rightValue}")
                loadDominoData()
            } catch (e: Exception) {
                Log.e("DominosActivity", "Ошибка при добавлении записи: ${e.message}")
            }
        }
    }

    fun openPlayersTableClick(view: View) {
        val intent = Intent(this, PlayersActivity::class.java)
        startActivity(intent)
    }

    fun openBoardTableClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
