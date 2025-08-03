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

class DessertFragment : Fragment() {
    private lateinit var adapter: FoodAdapter
    private val dessertImageResIds = listOf(
        R.drawable.dessert1, R.drawable.dessert2, R.drawable.dessert3,
        R.drawable.dessert4, R.drawable.dessert5, R.drawable.dessert6,
        R.drawable.dessert
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFoodItems)

        val desserts = FoodRepository.getAllFoods()
            .filter { it.imageRes in dessertImageResIds }

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val isAdmin = activity?.let { it is admin_page } ?: false

        adapter = FoodAdapter(desserts) { selectedFood ->
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
            .filter { it.imageRes in dessertImageResIds }
            .sortedByDescending { it.rating } // Optional: Add sorting
        adapter.updateList(updatedList)
    }
}