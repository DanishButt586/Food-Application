package com.example.foodapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LanguageAdapter(
    private val languages: List<language>,
    private val onSelect: (language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LangViewHolder>() {

    inner class LangViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.flagIcon)
        val name: TextView = view.findViewById(R.id.langName)
        val check: ImageView = view.findViewById(R.id.langCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)
        return LangViewHolder(view)
    }

    override fun onBindViewHolder(holder: LangViewHolder, position: Int) {
        val lang = languages[position]
        holder.icon.setImageResource(lang.iconResId)
        holder.name.text = lang.name
        holder.check.visibility = if (lang.isSelected) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            languages.forEach { it.isSelected = false }
            lang.isSelected = true
            notifyDataSetChanged()
            onSelect(lang)
        }
    }

    override fun getItemCount(): Int = languages.size
}
