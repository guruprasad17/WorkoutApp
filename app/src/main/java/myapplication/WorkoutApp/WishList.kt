package myapplication.WorkoutApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import myapplication.WorkoutApp.databinding.ActivityWishListBinding
import java.util.*

class WishList : AppCompatActivity(), WishListItemDisplayAdapter.OnClickListener {

    private var binding: ActivityWishListBinding? = null
    private var wishListItemAdapter: WishListItemDisplayAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarWishListActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val dao = (application as WorkOutApp).db!!.WishListDao()
        displayWishListItems(dao)


    }


    fun displayWishListItems(wishListDao: WishListDao){
        val daoFlow = wishListDao.fetchAllWishListItem().distinctUntilChanged()
        lifecycleScope.launch {
            daoFlow.collect { allWishListItemList ->

                if (allWishListItemList != null && allWishListItemList.isNotEmpty()) {
                    Log.e("details", "$allWishListItemList")
                    val list = ArrayList(allWishListItemList)

                    Log.e("inside recycler", "abkdakd")
                    wishListItemAdapter = WishListItemDisplayAdapter(list)
                    binding?.wishListStatus?.layoutManager =
                        LinearLayoutManager(this@WishList, LinearLayoutManager.VERTICAL, false)
                    binding?.wishListStatus?.adapter = wishListItemAdapter
                    wishListItemAdapter!!.setOnClickListener(this@WishList)


                } else {

                    binding?.wishListStatus?.adapter = null
                }

            }
        }

    }

    override fun onClick(position: WishListEntity) {
        val wishListdao = (application as WorkOutApp).db!!.WishListDao()
        lifecycleScope.launch{
            if(position!=null)
            {
                wishListdao.deleteWishListItem(position)
            }

        }
        displayWishListItems(wishListdao)
    }
}