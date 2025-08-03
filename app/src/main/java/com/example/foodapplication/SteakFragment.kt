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

class SteakFragment : Fragment() {
    private lateinit var adapter: FoodAdapter
    private val steakImageResIds = listOf(
        R.drawable.steak1, R.drawable.steak2, R.drawable.steak3,
        R.drawable.steak4, R.drawable.steak5, R.drawable.steak6,
        R.drawable.steak
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFoodItems)

        val steaks = FoodRepository.getAllFoods()
            .filter { it.imageRes in steakImageResIds }

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val isAdmin = activity?.let { it is admin_page } ?: false

        adapter = FoodAdapter(steaks) { selectedFood ->
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
            .filter { it.imageRes in steakImageResIds }
            .sortedByDescending { it.rating } // Optional: Add sorting
        adapter.updateList(updatedList)
    }
}