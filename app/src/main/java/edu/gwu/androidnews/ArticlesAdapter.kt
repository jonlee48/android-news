package edu.gwu.androidnews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ArticlesAdapter(val context: Context, val articles: List<Article>, val cellClickListener: CellClickListener) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    // How many rows (total) do you want the adapter to render?
    override fun getItemCount(): Int {
        return articles.size
    }

    // The RecyclerView needs a "fresh" / new row, so we need to:
    // 1. Read in the XML file for the row type
    // 2. Use the new row to build a ViewHolder to return
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // A LayoutInflater is an object that knows how to read & parse an XML file
        val layoutInflater = LayoutInflater.from(parent.context)

        // Read & parse the XML file to create a new row at runtime
        // The 'inflate' function returns a reference to the root layout (the "top" view in the hierarchy) in our newly created row
        val rootLayout: View = layoutInflater.inflate(R.layout.article, parent, false)

        // We can now create a ViewHolder from the root view
        return ViewHolder(rootLayout)
    }

    // The RecyclerView is ready to display a new (or recycled) row on the screen, represented a our ViewHolder.
    // We're given the row position / index that needs to be rendered.
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currArticle = articles[position]
        viewHolder.title.text = currArticle.title
        viewHolder.source.text = currArticle.source
        viewHolder.content.text = currArticle.content

        if (currArticle.iconUrl.isNotBlank()) {
            Picasso.get().setIndicatorsEnabled(true)

            Picasso
                .get()
                .load(currArticle.iconUrl)
                .into(viewHolder.icon)
        }
        viewHolder.itemView.setOnClickListener{
            cellClickListener.onCellClickListener(currArticle)
        }
    }

    // A ViewHolder represents the Views that comprise a single row in our list (e.g.
    // our row to display a Tweet contains three TextViews and one ImageView).
    //
    // The "rootLayout" passed into the constructor comes from onCreateViewHolder. From the root layout, we can
    // call findViewById to search through the hierarchy to find the Views we care about in our new row.
    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val title: TextView = rootLayout.findViewById(R.id.article_title)
        val source: TextView = rootLayout.findViewById(R.id.article_source)
        val content: TextView = rootLayout.findViewById(R.id.article_content)
        val icon: ImageView = rootLayout.findViewById(R.id.article_icon)
    }
}