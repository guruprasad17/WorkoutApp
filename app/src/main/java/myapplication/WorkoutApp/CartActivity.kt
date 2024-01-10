package myapplication.WorkoutApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.CurrencyType
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import myapplication.WorkoutApp.databinding.ActivityCartBinding
import java.util.*

class CartActivity : AppCompatActivity(), CartItemDisplayAdapter.OnClickListener {

    private var binding: ActivityCartBinding? = null
    private var cartItemAdapter: CartItemDisplayAdapter? = null

    private var mtotal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarCartActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val dao = (application as WorkOutApp).db!!.CartDao()
        displayCartItems(dao)

        binding?.buyButton?.setOnClickListener {
            BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                .setCurrency(CurrencyType.INR)
                .setDescription("Customer added item to cart")
                .setShipping(0.0)
                .setTax(18.0)
                .setRevenue(mtotal.toDouble())
                .addCustomDataProperty("user-id", "Custom_Event_Property_val1")
                .logEvent(applicationContext)

               lifecycleScope.launch {
                   dao.deleteAllItems()
               }

        }
    }

    fun displayCartItems(cartDao: CartDao) {
        Log.e("displayCartItems","called")

        val daoFlow = cartDao.fetchAllCartItem().distinctUntilChanged()
        lifecycleScope.launch {
            daoFlow.collect { allCartItemList ->

                if (allCartItemList != null && allCartItemList.isNotEmpty()) {
                    Log.e("details", "$allCartItemList")
                    val list = ArrayList(allCartItemList)
                    // Creates a vertical Layout Manager

                    mtotal=0
                    // History adapter is initialized and the list is passed in the param.
                    for (item in list) {

                        Log.e("items", "${item.image}")
                        mtotal += (item.price * item.quantity)

                    }
                    Log.e("inside recycler", "abkdakd")
                    cartItemAdapter = CartItemDisplayAdapter(list, applicationContext)
                    binding?.cartListStatus?.layoutManager =
                        LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                    binding?.priceAmount?.text = mtotal.toString()
                    binding?.cartListStatus?.adapter = cartItemAdapter
                    cartItemAdapter!!.setOnClickListener(this@CartActivity)


                } else {
                    mtotal = 0
                    binding?.priceAmount?.text = mtotal.toString()
                    binding?.cartListStatus?.adapter = null
                }

            }
        }

    }




    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onClick(position: CartEntity, id: Int) {
        val dao = (application as WorkOutApp).db!!.CartDao()
        val wishListdao = (application as WorkOutApp).db!!.WishListDao()

        lifecycleScope.launch{

            if(position!=null && id==1)
            {
                dao.deleteCartItem(position)

            }

            if(position!=null && id==2)
            {
                val wishListItem = WishListEntity(
                    id = position.id,
                    name = position.name,
                    image = position.image,
                    price = position.price,
                    quantity = position.quantity
                )
                wishListdao.insertOrUpdateWishListItem(wishListItem)
                dao.deleteCartItem(position)

            }

        }
        displayCartItems(dao)
        Log.e("delete","deleted")
    }

}