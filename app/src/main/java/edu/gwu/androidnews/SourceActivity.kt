package edu.gwu.androidnews

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.doAsync

class SourceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, SourceClickListener {

    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var skipButton: Button

    private val categories = arrayOf(
        "Business",
        "Entertainment",
        "General",
        "Health",
        "Science",
        "Sports",
        "Technology"
    )
    private var category = categories[0]
    private var term: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)

        // Get the search term from the intent key-value pair
        val intent: Intent = getIntent()
        val temp: String? = intent.getStringExtra("TERM")
        if (temp != null)
            term = temp
        val title = getString(R.string.source_title, term)
        setTitle(title)

        skipButton = findViewById(R.id.skip_button)

        // Setup spinner
        spinner = findViewById(R.id.spinner)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.setAdapter(spinnerAdapter)

        spinner.setSelection(0)
        spinner.onItemSelectedListener = this

        skipButton.setOnClickListener {
            val intent: Intent = Intent( this, ResultsActivity::class.java)
            intent.putExtra("CATEGORY", category)
            intent.putExtra("QUERY", term)
            startActivity(intent)
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        category = categories[p2]
        //make API call
        doAsync {
            val newsManager = NewsManager()

            // make call to news API
            val apiKey = getString(R.string.news_api_key)
            Log.d("SourceActivity", "Retrieving sources on ${categories[p2]}")

            val sources: List<Source> = newsManager.retrieveSources(apiKey, categories[p2])
            runOnUiThread {
                if (sources.isNotEmpty()) {
                    recyclerView = findViewById(R.id.results_recycler_viewer)

                    // Sets scrolling direction to vertical
                    recyclerView.layoutManager = LinearLayoutManager(this@SourceActivity)

                    val adapter = SourceAdapter(this@SourceActivity, sources, this@SourceActivity)
                    recyclerView.adapter = adapter
                    Log.d("SourceActivity", "Updating recycler view")
                } else {
                    Toast.makeText(
                        this@SourceActivity,
                        getString(R.string.no_sources),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onSourceClickListener(data: Source) {
        val intent: Intent = Intent( this, ResultsActivity::class.java)
        intent.putExtra("SOURCE_ID", data.id)
        intent.putExtra("SOURCE_NAME", data.name)
        intent.putExtra("QUERY", term)
        startActivity(intent)
    }
}