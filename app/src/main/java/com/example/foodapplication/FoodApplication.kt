package com.example.foodapplication

import android.app.Application

class FoodApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        loadInitialData()
    }

    private fun loadInitialData() {
        val initialFoods = listOf(
            // Burgers
            Food(R.drawable.burger1, "Classic Beef Burger", 4.9, "Rs. 850", "Juicy beef patty with fresh vegetables", true),
            Food(R.drawable.burger2, "Cheese Supreme", 4.8, "Rs. 950", "Melted cheese over a beef patty", true),
            Food(R.drawable.burger3, "BBQ Bacon Burger", 4.7, "Rs. 1,100", "Smoky BBQ sauce with crispy bacon", true),
            Food(R.drawable.burger4, "Veggie Delight", 4.6, "Rs. 750", "Grilled vegetables with special sauce", true),
            Food(R.drawable.burger5, "Double Patty Blast", 5.0, "Rs. 1,250", "Two patties for double the flavor", true),
            Food(R.drawable.burger6, "Spicy Chicken", 4.8, "Rs. 900", "Crispy chicken with hot spices", true),

            Food(R.drawable.dessert1, "Chocolate Cake", 4.9, "Rs. 450", "Rich chocolate layer cake", true),
            Food(R.drawable.dessert2, "Ice Cream Sundae", 4.8, "Rs. 350", "Vanilla ice cream with chocolate sauce and nuts", true),
            Food(R.drawable.dessert3, "Gulab Jamun", 5.0, "Rs. 250", "Soft milk-solid dumplings soaked in sweet syrup", true),
            Food(R.drawable.dessert4, "Tiramisu", 4.7, "Rs. 550", "Coffee-flavored Italian dessert with mascarpone", true),
            Food(R.drawable.dessert5, "Fruit Tart", 4.6, "Rs. 400", "Assorted fresh fruits on creamy custard in a pastry", true),
            Food(R.drawable.dessert6, "Cheesecake", 4.9, "Rs. 650", "Creamy cheesecake with a graham cracker crust", true),

            Food(R.drawable.steak1, "Grilled Ribeye Steak", 4.9, "Rs. 2,500", "Premium quality ribeye steak with charred edges", true),
            Food(R.drawable.steak2, "Smoked BBQ Brisket", 4.8, "Rs. 2,800", "Slow-smoked brisket with rich BBQ glaze", true),
            Food(R.drawable.steak3, "Pan-Seared Garlic Butter Sirloin", 4.7, "Rs. 2,400", "Tender sirloin seared in garlic butter", true),
            Food(R.drawable.steak4, "Spicy Moroccan Lamb Chops", 4.9, "Rs. 2,600", "Chops marinated in Moroccan spices with a kick", true),
            Food(R.drawable.steak5, "Shrimp Scampi", 5.0, "Rs. 2,900", "Succulent shrimp in garlic-lemon butter sauce", true),
            Food(R.drawable.steak6, "Spicy Buffalo Wings", 4.8, "Rs. 2,700", "Crispy wings tossed in tangy buffalo hot sauce", true),

            Food(R.drawable.sandwich1, "Chicago Style Hot Dog", 4.8, "Rs. 450", "Authentic Chicago-style hot dog with mustard, onions & relish", true),
            Food(R.drawable.sandwich2, "Chili Cheese Dog", 4.7, "Rs. 350", "Hot dog topped with hearty chili and melted cheese", true),
            Food(R.drawable.sandwich3, "Classic Beef Hot Dog", 4.9, "Rs. 500", "Juicy beef frankfurter in a soft bun with ketchup & mustard", true),
            Food(R.drawable.sandwich4, "Hawaiian BBQ Dog", 4.6, "Rs. 400", "Grilled dog glazed in sweet BBQ sauce with pineapple", true),
            Food(R.drawable.sandwich5, "Reuben Style Hot Dog", 4.8, "Rs. 550", "Hot dog topped with sauerkraut, Swiss cheese & Thousand Island", true),
            Food(R.drawable.sandwich6, "Spicy Tuna Roll", 4.7, "Rs. 380", "Spicy tuna filling wrapped in seaweed and rice", true),

            Food(R.drawable.drink1, "Coconut Water", 4.8, "Rs. 150", "Fresh natural coconut water", true),
            Food(R.drawable.drink2, "Fresh Orange Juice", 4.9, "Rs. 200", "Cold-pressed orange juice with pulp", true),
            Food(R.drawable.drink3, "Mint Lemonade", 4.7, "Rs. 250", "Lemonade infused with fresh mint leaves", true),
            Food(R.drawable.drink4, "Mango Tango Slush", 4.6, "Rs. 180", "Blended mango slush topped with mango chunks", true),
            Food(R.drawable.drink5, "Iced Caramel Macchiato", 4.8, "Rs. 220", "Espresso with milk, ice and caramel drizzle", true),
            Food(R.drawable.drink6, "Berry Blast Smoothie", 4.9, "Rs. 280", "Mixed berries blended with yogurt", true),

            Food(R.drawable.pizza1, "BBQ Chicken Delight", 4.9, "Rs. 1,200", "Smoky BBQ chicken pizza with tangy sauce", true),
            Food(R.drawable.pizza2, "Hawaiian Paradise", 4.8, "Rs. 1,450", "Pineapple and ham on a cheesy crust", true),
            Food(R.drawable.pizza3, "Margherita", 4.7, "Rs. 1,350", "Classic tomato, mozzarella & basil pizza", true),
            Food(R.drawable.pizza4, "Meat Feast Pizza", 4.6, "Rs. 1,100", "Loaded with pepperoni, sausage & bacon", true),
            Food(R.drawable.pizza5, "Pepperoni Lovers", 4.9, "Rs. 1,550", "Extra pepperoni slices on a crispy base", true),
            Food(R.drawable.pizza6, "Veggie Supreme", 4.8, "Rs. 1,300", "Bell peppers, olives, onions & mushrooms", true)
            
        )
        FoodRepository.addFoods(initialFoods)
    }
}