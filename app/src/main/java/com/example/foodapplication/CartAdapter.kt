package com.example.foodapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapplication.databinding.CartItemRowBinding

class CartAdapter(
    private var cartItems: MutableList<Food>,
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: CartItemRowBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        with(holder.binding) {
            // Load image & name
            ivItemImage.setImageResource(item.imageRes)
            tvItemName.text = item.name

            // Show unit price (static)
            val unitPrice = item.getPriceInt()
            tvItemPrice.text = "₨%,d".format(unitPrice)

            // Quantity
            tvQuantity.text = item.quantity.toString()

            // Plus: add one
            btnPlus.setOnClickListener {
                CartManager.addToCart(item.copy(quantity = 1))
                refreshList()
            }

            // Minus: remove one (or remove row if reaches zero)
            btnMinus.setOnClickListener {
                CartManager.decrementFromCart(item)
                refreshList()
            }

            // Delete: remove entire item
            btnDelete.setOnClickListener {
                CartManager.removeFromCart(item)
                refreshList()
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshList() {
        // Reload from CartManager and notify
        cartItems = CartManager.getCartItems().toMutableList()
        notifyDataSetChanged()
        onCartChanged()
    }
}
