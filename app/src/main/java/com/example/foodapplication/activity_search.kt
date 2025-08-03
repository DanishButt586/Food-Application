package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

@Suppress("DEPRECATION")
class activity_search : AppCompatActivity() {

    private lateinit var searchAdapter: FoodAdapter
    private lateinit var recentSearchesAdapter: SearchHistoryAdapter
    private lateinit var allFoods: List<Food>
    private val searchHistory = mutableListOf<Pair<String, Long>>()
    private val sharedPrefs by lazy { getSharedPreferences("SEARCH_HISTORY", Context.MODE_PRIVATE) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val rvSearchResults = findViewById<RecyclerView>(R.id.rvSearchResults)
        val rvRecentSearches = findViewById<RecyclerView>(R.id.rvRecentSearches)
        val tvRecentSearches = findViewById<TextView>(R.id.tvRecentSearches)
        val backIcon: ImageView = findViewById(R.id.imageView4)

        backIcon.setOnClickListener {
            onBackPressed()
        }

        allFoods = FoodRepository.getAllFoods()

        searchAdapter = FoodAdapter(emptyList()) { selectedFood ->
            val intent = Intent(this, activity_food_detail::class.java).apply {
                putExtra("SELECTED_FOOD", selectedFood)
            }
            startActivity(intent)
        }

        recentSearchesAdapter = SearchHistoryAdapter(mutableListOf(),
            onDeleteClick = { position ->
                searchHistory.removeAt(position)
                saveSearchHistory()
                updateRecentSearchesVisibility()
                recentSearchesAdapter.updateList(searchHistory)
            },
            onItemClick = { term ->
                etSearch.setText(term)
                etSearch.setSelection(term.length)
                addToSearchHistory(term)
                filterResults(term)
                showSearchResults()
                hideKeyboard(etSearch)
            }
        )

        rvSearchResults.layoutManager = LinearLayoutManager(this)
        rvSearchResults.adapter = searchAdapter

        rvRecentSearches.layoutManager = LinearLayoutManager(this)
        rvRecentSearches.adapter = recentSearchesAdapter

        loadSearchHistory()
        recentSearchesAdapter.updateList(searchHistory)
        updateRecentSearchesVisibility()

        etSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = etSearch.compoundDrawables[drawableEnd]
                if (drawable != null && event.rawX >= (etSearch.right - drawable.bounds.width() - etSearch.paddingEnd)) {
                    val query = etSearch.text.toString().trim()
                    if (query.isNotEmpty()) {
                        addToSearchHistory(query)
                        filterResults(query)
                        showSearchResults()
                        hideKeyboard(etSearch)
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isEmpty()) {
                    showRecentSearches()
                }
            }
        })
    }

    private fun loadSearchHistory() {
        val json = sharedPrefs.getString("HISTORY", null)
        json?.let {
            val type = object : TypeToken<MutableList<Pair<String, Long>>>() {}.type
            searchHistory.addAll(Gson().fromJson(it, type))
        }
    }

    private fun saveSearchHistory() {
        sharedPrefs.edit { putString("HISTORY", Gson().toJson(searchHistory)) }
    }

    private fun addToSearchHistory(query: String) {
        searchHistory.removeAll { it.first.equals(query, true) }
        searchHistory.add(0, Pair(query, System.currentTimeMillis()))
        if (searchHistory.size > 10) searchHistory.removeAt(searchHistory.lastIndex)
        saveSearchHistory()
    }

    private fun filterResults(query: String) {
        val filtered = allFoods
            .filter { it.name.contains(query, true) }
            .distinctBy { it.name }   // remove duplicates by name
        searchAdapter.updateList(filtered)
    }

    private fun updateRecentSearchesVisibility() {
        findViewById<TextView>(R.id.tvRecentSearches).visibility =
            if (searchHistory.isEmpty()) View.GONE else View.VISIBLE
        findViewById<RecyclerView>(R.id.rvRecentSearches).visibility =
            if (searchHistory.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showSearchResults() {
        findViewById<RecyclerView>(R.id.rvSearchResults).visibility = View.VISIBLE
        findViewById<RecyclerView>(R.id.rvRecentSearches).visibility = View.GONE
        findViewById<TextView>(R.id.tvRecentSearches).visibility = View.GONE
    }

    private fun showRecentSearches() {
        findViewById<RecyclerView>(R.id.rvSearchResults).visibility = View.GONE
        recentSearchesAdapter.updateList(searchHistory)
        updateRecentSearchesVisibility()
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
