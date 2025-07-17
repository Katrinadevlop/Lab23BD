package com.example.lab23bd.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.lab23bd.data.database.DominoesDbEntity
import kotlinx.coroutines.launch

class DominoesActivity : AppCompatActivity() {
    private lateinit var repository: DaoRepository
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseModule.init(applicationContext)
        setContentView(R.layout.activity_dominos)

        gridView = findViewById(R.id.gridView)
        repository = DaoRepository(DatabaseModule.getDatabase().getBoardDao())

        loadDominoData()
    }

    private fun loadDominoData() {
        lifecycleScope.launch {
            try {
                val dominoData = repository.getAllDominoData()
                val adapter = ArrayAdapter(
                    this@DominoesActivity,
                    android.R.layout.simple_list_item_1,
                    dominoData.map {
                        "ID: ${it.id}, Левая сторона: ${it.leftValue}, Правая сторона: ${it.rightValue}"
                    }
                )
                gridView.adapter = adapter
            } catch (e: Exception) {
                Log.e("DominoesActivity", "Ошибка загрузки данных: ${e.message}")
                Toast.makeText(this@DominoesActivity, "Ошибка загрузки данных", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addClick(view: View) {
        val idEditText = findViewById<EditText>(R.id.idDomino)
        val leftScoreEditText = findViewById<EditText>(R.id.leftScore)
        val rightScoreEditText = findViewById<EditText>(R.id.rightScore)

        val idText = idEditText.text.toString().trim()
        val leftText = leftScoreEditText.text.toString().trim()
        val rightText = rightScoreEditText.text.toString().trim()

        if (idText.isBlank() || leftText.isBlank() || rightText.isBlank()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_LONG).show()
            return
        }

        val id = idText.toIntOrNull()
        val left = leftText.toIntOrNull()
        val right = rightText.toIntOrNull()

        if (id == null || id <= 0) {
            Toast.makeText(this, "ID должен быть положительным числом", Toast.LENGTH_LONG).show()
            return
        }

        if (left == null || left < 0 || right == null || right < 0) {
            Toast.makeText(this, "Значения сторон должны быть неотрицательными числами", Toast.LENGTH_LONG).show()
            return
        }

        val dominos = DominoesDbEntity(id = id, leftValue = left, rightValue = right)

        lifecycleScope.launch {
            try {
                repository.insertNewDomino(dominos)
                Toast.makeText(this@DominoesActivity, "Домино добавлено", Toast.LENGTH_SHORT).show()
                Log.d("DominoesActivity", "Добавлено: ID=${dominos.id}, Л=${dominos.leftValue}, П=${dominos.rightValue}")
                loadDominoData()
                clearFields()
            } catch (e: Exception) {
                Log.e("DominoesActivity", "Ошибка при добавлении: ${e.message}")
                Toast.makeText(this@DominoesActivity, "Ошибка добавления: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearFields() {
        findViewById<EditText>(R.id.idDomino).text.clear()
        findViewById<EditText>(R.id.leftScore).text.clear()
        findViewById<EditText>(R.id.rightScore).text.clear()
    }

    fun openPlayersTableClick(view: View) {
        startActivity(Intent(this, PlayersActivity::class.java))
    }

    fun openBoardTableClick(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
