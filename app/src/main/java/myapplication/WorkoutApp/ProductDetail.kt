package myapplication.WorkoutApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.*
import kotlinx.coroutines.launch
import myapplication.WorkoutApp.databinding.ActivityProductDetailBinding

class ProductDetail : AppCompatActivity() {

    private var binding: ActivityProductDetailBinding? = null

    private var productToDisplay: ProductModel? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var buo : BranchUniversalObject
   var mDeepLink : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        sessionManager = SessionManager(this)

        val receivedIntent = intent
        productToDisplay = receivedIntent.getSerializableExtra("prod") as? ProductModel

        if(productToDisplay != null)
        {
            binding?.productImage?.setImageResource(productToDisplay!!.getImage())
            binding?.productName?.text = productToDisplay!!.getName()
            binding?.productPrice?.text = productToDisplay!!.getPrice()
            buo = BranchUniversalObject()
                .setCanonicalIdentifier(productToDisplay!!.getId().toString())
                .setContentMetadata(
                    ContentMetadata()
//                        .addCustomMetadata("custom_metadata_key1", "custom_metadata_val1")
//                        .addCustomMetadata("custom_metadata_key1", "custom_metadata_val1")
                        .addCustomMetadata("sharing_user_id", "${sessionManager.getUserId()}")
                        .setProductCategory(ProductCategory.HEALTH_AND_BEAUTY)
                        .setProductName(productToDisplay!!.getName())
                        .setProductCondition(ContentMetadata.CONDITION.EXCELLENT)
                        .setQuantity(1.0)
                        .setSku("test_sku")
                        .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
                )

        }

        binding?.addcartbutton?.setOnClickListener{


            BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_CART)
                .setCurrency(CurrencyType.INR)
                .setDescription("Customer added item to cart")
                .setShipping(0.0)
                .setTax(18.0)
                .setRevenue(productToDisplay!!.getPrice().toDouble())
                .setSearchQuery("Test Search query")
                .addContentItems(buo)
                .logEvent(applicationContext)
            val cartDao = (application as WorkOutApp).db?.CartDao()
            checkAndInsertOrUpdateCartItem(cartDao!!, productToDisplay!!)

        }
        var price = productToDisplay!!.getPrice()?.toDouble()
        if (price == null) {
            price= 0.0
        }
        binding?.sharebutton?.setOnClickListener{

            val lp = LinkProperties()
                .setChannel("messages")
                .setFeature("sharing")
                .setCampaign("7-minute-fitness")
                .addControlParameter("\$path", "ProdectDeatil")
                .addControlParameter("\$productId", "${productToDisplay!!.getId()}")

            val ss = ShareSheetStyle(this@ProductDetail, "Check this out!", "This stuff is awesome: ")
                .setCopyUrlStyle(ActivityCompat.getDrawable(this, R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(ActivityCompat.getDrawable(this, R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With")

            BranchEvent(BRANCH_STANDARD_EVENT.SHARE)
                .setCustomerEventAlias("my_custom_alias")
                .setDescription("Product SHARE")
                .setSearchQuery(productToDisplay!!.getName())
                .addCustomDataProperty("user-id", sessionManager.getUserId().toString())
                .addContentItems(buo)
                .logEvent(applicationContext);

            buo.generateShortUrl(this, lp, Branch.BranchLinkCreateListener {
                    url, error ->
                if (error == null) {
                    mDeepLink = url.toString()
                    Log.i("BRANCH SDK", "got my Branch link to share in productDetail: " + mDeepLink)
//                    shareLink()
                    buo.showShareSheet(this, lp, ss, object : Branch.BranchLinkShareListener {
                        override fun onShareLinkDialogLaunched() {}
                        override fun onShareLinkDialogDismissed() {}
                        override fun onLinkShareResponse(
                            sharedLink: String?,
                            sharedChannel: String?,
                            error: BranchError?
                        ) {
                        }

                        override fun onChannelSelected(channelName: String) {}
                    })
                }
                else
                {
                    Log.i("link error prdctDetail", error.toString())
                }
            })


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