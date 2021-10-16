package edu.gwu.androidnews

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.doAsync

class SourceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView

    val categories  = arrayOf(
        "Business",
        "Entertainment",
        "General",
        "Health",
        "Science",
        "Sports",
        "Technology"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)

        // Get the search term from the intent key-value pair
        val intent: Intent = getIntent()
        val term: String? = intent.getStringExtra("TERM")
        val title = getString(R.string.source_title, term)
        setTitle(title)

        // Setup spinner
        spinner = findViewById(R.id.spinner)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.setAdapter(spinnerAdapter)

        spinner.setSelection(0)
        spinner.onItemSelectedListener = this
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //make API call
        doAsync {
            val newsManager = NewsManager()

            // make call to news API
            val apiKey = getString(R.string.news_api_key)
            Log.d("SourceActivity", "Retrieving sources on ${categories[p2]}")

            val sources: List<Source> = newsManager.retrieveSources(apiKey, categories[p2])
            runOnUiThread {
                recyclerView = findViewById(R.id.sources_recycler_viewer)

                // Sets scrolling direction to vertical
                recyclerView.layoutManager = LinearLayoutManager(this@SourceActivity)

                val adapter = SourceAdapter(sources)
                recyclerView.adapter = adapter
                Log.d("SourceActivity", "Updating recycler view")
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    fun generateFakeSource(): List<Source> {
        return listOf(
            Source(
                name="ACB News",
                description="Description of ABC News. Blah blah blah."
            ),
            Source(
                name="Business Insider",
                description="Description of Business Insider. Blah blah blah."
            ),
            Source(
                name="CNN",
                description="Description of CNN. Blah blah blah."
            ),
            Source(
                name="Fox News",
                description="Description of Fox News. Blah blah blah."
            ),
            Source(
                name="Guardian",
                description="Description of Guardian. Blah blah blah."
            ),
            Source(
                name="Huffington Post",
                description="Description of Huffington Post. Blah blah blah."
            ),
            Source(
                name="Los Angeles Times",
                description="Description of Los Angeles Times. Blah blah blah."
            ),
            Source(
                name="Politico",
                description="Description of Politico. Blah blah blah."
            ),
            Source(
                name="Star Gazette",
                description="Description of Star Gazette. Blah blah blah."
            ),
            Source(
                name="The New York Times",
                description="Description of The New York Times. Blah blah blah."
            ),
            Source(
                name="Wall Street Journal",
                description="Description of Wall Street Journal. Blah blah blah."
            ),
        )
    }

}