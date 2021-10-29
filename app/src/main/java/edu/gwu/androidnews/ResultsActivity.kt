package edu.gwu.androidnews

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.doAsync

class ResultsActivity : AppCompatActivity(), ArticleClickListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // Get the search term from the intent key-value pair
        val intent: Intent = getIntent()
        val id: String? = intent.getStringExtra("SOURCE_ID")
        val name: String? = intent.getStringExtra("SOURCE_NAME")
        val query: String? = intent.getStringExtra("QUERY")
        var articles: List<Article> = listOf()

        val newsManager = NewsManager()
        val apiKey = getString(R.string.news_api_key)
        doAsync {
            if (!id.isNullOrBlank()) {
                Log.d("ResultsActivity", "Detailed search")
                val title = getString(R.string.results_title_source, name, query)
                setTitle(title)
                if (query != null && id != null) {
                    articles = newsManager.retrieveArticles(apiKey, query, id)
                    runOnUiThread {
                        recyclerView = findViewById(R.id.results_recycler_viewer)

                        // Sets scrolling direction to vertical
                        recyclerView.layoutManager = LinearLayoutManager(this@ResultsActivity)

                        Log.d("ResultsActivity", "Retrieving articles from $query")
                        val adapter = ArticlesAdapter(this@ResultsActivity, articles, this@ResultsActivity)
                        recyclerView.adapter = adapter
                    }
                }
            } else {
                val title = getString(R.string.results_title, query)
                setTitle(title)
                Log.d("ResultsActivity", "Generic search1")
                if (query != null) {
                    Log.d("ResultsActivity", "Generic search2")
                    articles = newsManager.retrieveArticles(apiKey, query)
                    runOnUiThread {
                        recyclerView = findViewById(R.id.results_recycler_viewer)

                        // Sets scrolling direction to vertical
                        recyclerView.layoutManager = LinearLayoutManager(this@ResultsActivity)

                        Log.d("ResultsActivity", "Retrieving articles from $query")
                        val adapter = ArticlesAdapter(this@ResultsActivity, articles, this@ResultsActivity)
                        recyclerView.adapter = adapter
                    }
                }
            }
        }


    }

    override fun onArticleClickListener(data: Article) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
        startActivity(browserIntent)
    }

}