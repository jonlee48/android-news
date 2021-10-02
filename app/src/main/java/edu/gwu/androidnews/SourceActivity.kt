package edu.gwu.androidnews

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class SourceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)

        val intent: Intent = getIntent()
        val term: String? = intent.getStringExtra("LOCATION")
        val title = getString(R.string.source_title, term)
        setTitle(title)

        val sources: List<Source> = generateFakeSource()
        recyclerView = findViewById(R.id.recycler_viewer)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        val adapter: SourceAdapter = SourceAdapter(sources)
        recyclerView.setAdapter(adapter)
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