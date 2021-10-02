package edu.gwu.androidnews

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {

    private lateinit var searchTerm: EditText
    private lateinit var searchButton: Button
    private lateinit var mapButton: Button
    private lateinit var headlinesButton: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set variables to IDs
        searchTerm = findViewById(R.id.search_edit_text)
        searchButton = findViewById(R.id.search_button)
        mapButton = findViewById(R.id.map_button)
        headlinesButton = findViewById(R.id.headlines_button)
        progressBar = findViewById(R.id.progress_bar)

        // Shared Preferences for saving/restoring search term
        val preferences: SharedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)
        val savedTerm = preferences.getString("SEARCH_TERM","")
        searchTerm.setText(savedTerm)

        // Set initial state of button
        val inputtedTerm: String = searchTerm.text.toString()
        val enableButton: Boolean = inputtedTerm.isNotBlank()
        searchButton.isEnabled = enableButton

        // Set button on-click listener
        searchButton.setOnClickListener {
            // Get and save search term
            val inputtedTerm = searchTerm.getText().toString()
            val editor = preferences.edit()
            editor.putString("SEARCH_TERM", inputtedTerm)
            editor.apply()

            Log.d("MainActivity", "Search Button Clicked")
            progressBar.visibility = View.VISIBLE

            // Create intent to open activity
            val intent: Intent = Intent( this, SourceActivity::class.java)
            intent.putExtra("TERM", inputtedTerm)

            startActivity(intent)
        }
        searchTerm.addTextChangedListener(textWatcher)
    }

    private val textWatcher: TextWatcher = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            Log.d("MainActivity", "Text is ${searchTerm.getText().toString()}")
            val inputtedTerm: String = searchTerm.text.toString()
            val enableButton: Boolean = inputtedTerm.isNotBlank()
            searchButton.isEnabled = enableButton
        }

        override fun afterTextChanged(p0: Editable?) {}

    }
}