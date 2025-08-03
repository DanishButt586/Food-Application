package com.example.foodapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private var foods: List<Food>,
    private val onItemClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Food>) {
        foods = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFood: ImageView = view.findViewById(R.id.ivFood)
        val ivFavorite: ImageView = view.findViewById(R.id.ivFavorite)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = foods.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]

        holder.ivFood.setImageResource(food.imageRes)
        holder.tvTitle.text = food.name
        holder.tvRating.text = "★ ${food.rating}"
        holder.tvPrice.text = food.price

        // ask the manager, not the model
        val fav = FavoriteManager.isFavorite(food)
        holder.ivFavorite.setImageResource(
            if (fav) R.drawable.ic_heart_filled
            else R.drawable.ic_heart_outline
        )

        holder.ivFavorite.setOnClickListener {
            val nowFav = FavoriteManager.toggle(food)
            holder.ivFavorite.setImageResource(
                if (nowFav) R.drawable.ic_heart_filled
                else R.drawable.ic_heart_outline
            )
            val msg = if (nowFav)
                "${food.name} added to favorites"
            else
                "${food.name} removed from favorites"
            Toast.makeText(holder.itemView.context, msg, Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {
            onItemClick(food)
        }
    }
}
