package edu.gwu.androidnews

import android.os.Bundle
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class SourceActivity : AppCompatActivity() {

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

        // Setup recycler viewer
        val sources: List<Source> = generateFakeSource()
        recyclerView = findViewById(R.id.recycler_viewer)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        val sourceAdapter: SourceAdapter = SourceAdapter(sources)
        recyclerView.setAdapter(sourceAdapter)
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