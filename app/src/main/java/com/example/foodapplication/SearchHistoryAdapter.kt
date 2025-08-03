package com.example.foodapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchHistoryAdapter(
    private var searches: List<Pair<String, Long>>,
    private val onDeleteClick: (Int) -> Unit,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSearchTerm: TextView = view.findViewById(R.id.tvSearchTerm)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_history, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (term, timestamp) = searches[position]
        val date = Date(timestamp)
        val formattedDate = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(date)
        holder.tvSearchTerm.text = "$term\n$formattedDate"

        holder.ivDelete.setOnClickListener {
            onDeleteClick(position)
        }

        holder.itemView.setOnClickListener {
            onItemClick(term)
        }
    }

    override fun getItemCount() = searches.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Pair<String, Long>>) {
        searches = newList
        notifyDataSetChanged()
    }
}