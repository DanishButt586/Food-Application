package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BurgerFragment : Fragment() {
    private lateinit var adapter: FoodAdapter
    private val burgerImageResIds = listOf(
        R.drawable.burger1, R.drawable.burger2, R.drawable.burger3,
        R.drawable.burger4, R.drawable.burger5, R.drawable.burger6,
        R.drawable.burger
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFoodItems)

        val burgerList = FoodRepository.getAllFoods()
            .filter { it.imageRes in burgerImageResIds }

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val isAdmin = activity?.let { it is admin_page } ?: false

        adapter = FoodAdapter(burgerList) { selectedFood ->
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
            .filter { it.imageRes in burgerImageResIds }
            .sortedByDescending { it.rating } // Optional: Add sorting
        adapter.updateList(updatedList)
    }

}
