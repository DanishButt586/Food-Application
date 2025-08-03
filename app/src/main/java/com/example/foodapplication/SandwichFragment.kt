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

class SandwichFragment : Fragment() {
    private lateinit var adapter: FoodAdapter
    private val sandwichImageResIds = listOf(
        R.drawable.sandwich1, R.drawable.sandwich2, R.drawable.sandwich3,
        R.drawable.sandwich4, R.drawable.sandwich5, R.drawable.sandwich6,
        R.drawable.sandwich
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFoodItems)

        val sandwiches = FoodRepository.getAllFoods()
            .filter { it.imageRes in sandwichImageResIds }

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val isAdmin = activity?.let { it is admin_page } ?: false

        adapter = FoodAdapter(sandwiches) { selectedFood ->
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
            .filter { it.imageRes in sandwichImageResIds }
            .sortedByDescending { it.rating } // Optional: Add sorting
        adapter.updateList(updatedList)
    }
}