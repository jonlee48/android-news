package edu.gwu.androidnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SourceAdapter(val sources: List<Source>) : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return sources.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currSource = sources[position]
        holder.name.setText(currSource.name)
        holder.description.setText(currSource.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.row_source, parent, false)
        return ViewHolder(rootLayout)
    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val name: TextView = rootLayout.findViewById(R.id.source_name)
        val description: TextView = rootLayout.findViewById(R.id.source_description)
    }
}