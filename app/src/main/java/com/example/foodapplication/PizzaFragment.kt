package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PizzaFragment : Fragment() {
    private lateinit var adapter: FoodAdapter
    private val pizzaImageResIds = listOf(
        R.drawable.pizza1, R.drawable.pizza2, R.drawable.pizza3,
        R.drawable.pizza4, R.drawable.pizza5, R.drawable.pizza6,
        R.drawable.pizza
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFoodItems)

        val pizzas = FoodRepository.getAllFoods()
            .filter { it.imageRes in pizzaImageResIds }

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val isAdmin = activity?.let { it is admin_page } ?: false

        adapter = FoodAdapter(pizzas) { selectedFood ->
            val intent = if (isAdmin) {
                Intent(requireContext(), AdminFoodDetailActivity::class.java).apply {
                    putExtra("food", selectedFood)
                }
            } else {
                Intent(requireContext(), activity_food_detail::class.java).apply {
                    putExtra("SELECTED_FOOD", selectedFood)
                }
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val updatedList = FoodRepository.getAllFoods()
            .filter { it.imageRes in pizzaImageResIds }
            .sortedByDescending { it.rating } // Optional: Add sorting
        adapter.updateList(updatedList)
    }
}