package myapplication.WorkoutApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import myapplication.WorkoutApp.databinding.ActivityProductDetailBinding

class ProductDetail : AppCompatActivity() {

    private var binding: ActivityProductDetailBinding? = null

    private var productToDisplay: ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        val receivedIntent = intent
        productToDisplay = receivedIntent.getSerializableExtra("prod") as? ProductModel

        if(productToDisplay != null)
        {
            binding?.productImage?.setImageResource(productToDisplay!!.getImage())
            binding?.productName?.text = productToDisplay!!.getName()
            binding?.productPrice?.text = productToDisplay!!.getPrice()

        }

        binding?.addcartbutton?.setOnClickListener{
            val cartDao = (application as WorkOutApp).db?.CartDao()
            checkAndInsertOrUpdateCartItem(cartDao!!, productToDisplay!!)

        }



    }

    fun checkAndInsertOrUpdateCartItem(cartDao:CartDao, product: ProductModel ) {
        val productId = product.getId().toString()
        lifecycleScope.launch {

            val existingCartItem = cartDao.getCartItem(productId)
            Log.e("notPresent", " $existingCartItem")
            if (existingCartItem != null) {

                // If the product ID exists, increase the count by 1
                existingCartItem.quantity++
                cartDao.insertOrUpdateCartItem(existingCartItem)
                Log.e("notPresent", " not present")
            } else {

                // If the product ID doesn't exist, insert a new row with count as 1
                val id: String = product.getId().toString()
                val name: String = product.getName()
                val image: Int = product.getImage()
                val price: Int = Integer.parseInt(product.getPrice())
                val newCartItem = CartEntity(id, name, image, price, 1)
                cartDao.insertOrUpdateCartItem(newCartItem)
                Log.e("notPresent", " not present complete")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}