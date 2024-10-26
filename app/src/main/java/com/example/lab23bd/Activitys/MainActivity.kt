package com.example.lab23bd.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.ListView
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
import com.example.lab23bd.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var repository: DaoRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.init(applicationContext)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db").build()
        val boardDao = db.getBoardDao()
        repository = DaoRepository(boardDao)
        val listView = findViewById<GridView>(R.id._dynamic)
        lifecycleScope.launch {
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
            listView.adapter = adapter
        }
        Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
    }

    fun openPlayersTableClick(view: View) {
        val intent = Intent(this, PlayersActivity::class.java)
        startActivity(intent)
    }

    fun openDominoesTableClick(view: View) {
        val intent = Intent(this, DominosActivity::class.java)
        startActivity(intent)
    }
}
