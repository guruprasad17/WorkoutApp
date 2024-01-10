package myapplication.WorkoutApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.CurrencyType

import myapplication.WorkoutApp.databinding.ActivityProductsListBinding
import java.util.*

class ProductsList : AppCompatActivity() {

    private var binding: ActivityProductsListBinding? = null
    private var productsAdapter: ProductsDisplayAdapter? = null
    private var productsList: ArrayList<ProductModel>? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsListBinding.inflate(layoutInflater)
        sessionManager = SessionManager(this)
        setContentView(binding?.root)
        val toolbar = binding?.appBar
        setSupportActionBar(toolbar)

        productsList = products.defaultProductList()
        setupProductsStatusRecyclerView()

    }

    private fun setupProductsStatusRecyclerView() {

        // Defining a layout manager for the recycle view
        // Here we have used a LinearLayout Manager with horizontal scroll.
        binding?.productsListStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // As the adapter expects the exercises list and context so initialize it passing it.
        productsAdapter = ProductsDisplayAdapter(productsList!!)

        // Adapter class is attached to recycler view
        binding?.productsListStatus?.adapter = productsAdapter

        productsAdapter!!.setOnClickListener(object :
            ProductsDisplayAdapter.OnClickListener {
            override fun onClick(position: ProductModel) {

                BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM)
                    .setCurrency(CurrencyType.INR)
                    .setDescription(position.getName())
                    .setShipping(0.0)
                    .setTax(18.0)
                    .setRevenue(position.getPrice().toDouble())
                    .setSearchQuery("Test Search query")
                    .addCustomDataProperty("user-id", sessionManager.getUserId().toString())
                    .logEvent(applicationContext)

                val intent = Intent(this@ProductsList, ProductDetail::class.java)
                intent.putExtra("prod", position)
                startActivity(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                // Start Activity 1
                val intent = Intent(this, WishList::class.java)
                startActivity(intent)
                return true
            }
            R.id.cart -> {
                // Start Activity 2
                BranchEvent(BRANCH_STANDARD_EVENT.VIEW_CART)
                    .addCustomDataProperty("user-id", sessionManager.getUserId().toString())
                    .logEvent(applicationContext)
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                return true
            }
            // Handle other menu items if needed

            else -> return super.onOptionsItemSelected(item)
        }
    }
}