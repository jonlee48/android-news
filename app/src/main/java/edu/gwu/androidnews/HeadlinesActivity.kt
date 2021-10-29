package edu.gwu.androidnews

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.doAsync

class HeadlinesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, CellClickListener {

    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var currentPage: TextView

    private val categories  = arrayOf(
        "Business",
        "Entertainment",
        "General",
        "Health",
        "Science",
        "Sports",
        "Technology"
    )

    private var page = 1
    private var catIndex = 0

    override fun onCellClickListener(data: Article) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
        startActivity(browserIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headlines)
        setTitle(R.string.top_headlines)

        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        currentPage = findViewById(R.id.current_page)
        prevButton.isEnabled = false

        // Setup spinner
        spinner = findViewById(R.id.spinner)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.setAdapter(spinnerAdapter)

        // Shared Preferences for saving/restoring category
        val preferences: SharedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)
        val savedCategory = preferences.getString("CATEGORY","")

        if (savedCategory.isNullOrBlank()) {
            spinner.setSelection(0)
        } else {
            spinner.setSelection(savedCategory.toInt())
            catIndex = savedCategory.toInt()
        }
        spinner.onItemSelectedListener = this

        nextButton.setOnClickListener {
            page += 1
            prevButton.isEnabled = true
            currentPage.setText(getString(R.string.current_page, page.toString()))
            updateResults()
        }

        prevButton.setOnClickListener {
            page -= 1
            nextButton.isEnabled = true
            if (page == 1)
                prevButton.isEnabled = false
            currentPage.setText(getString(R.string.current_page, page.toString()))
            updateResults()

        }

        currentPage.setText(getString(R.string.current_page, page.toString()))
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val preferences: SharedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("CATEGORY", p2.toString())
        editor.apply()

        page = 1
        prevButton.isEnabled = false
        nextButton.isEnabled = true
        currentPage.setText(getString(R.string.current_page, page.toString()))
        catIndex = p2
        updateResults()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    fun updateResults() {
        //make API call
        doAsync {
            val newsManager = NewsManager()

            // make call to news API
            val apiKey = getString(R.string.news_api_key)
            Log.d("HeadlinesActivity", "Retrieving sources on ${categories[catIndex]}")

            val articles: List<Article> = newsManager.retrieveHeadlines(apiKey, categories[catIndex], page)
            runOnUiThread {
                if (articles.isNotEmpty()) {
                    recyclerView = findViewById(R.id.sources_recycler_viewer)

                    // Sets scrolling direction to vertical
                    recyclerView.layoutManager = LinearLayoutManager(this@HeadlinesActivity)

                    val adapter =
                        ArticlesAdapter(this@HeadlinesActivity, articles, this@HeadlinesActivity)
                    recyclerView.adapter = adapter
                    Log.d("HeadlinesActivity", "Updating recycler view")
                } else {
                    page -= 1
                    val toast = Toast.makeText(
                        this@HeadlinesActivity,
                        "No more results found.",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    nextButton.isEnabled = false
                }
            }
        }
    }
}