package myapplication.WorkoutApp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.*
import myapplication.WorkoutApp.databinding.ItemCartTileBinding
import java.util.*

class CartItemDisplayAdapter(val itemsCart: ArrayList<CartEntity>,  private val context: Context) :
    RecyclerView.Adapter<CartItemDisplayAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartTileBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model: CartEntity = itemsCart[position]
        var image: Int = model.image
        Log.e("model", "${model}")
        Log.e("variable", "${image}")
        holder.itemName.text = model.name
        holder.itemPrice.text = model.price.toString()
        holder.itemImage.setImageResource( model.image)
        holder.itemQuantity.text = model.quantity.toString()

        holder.remove.setOnClickListener {
            Log.e("clicked", "$onClickListener")
            if (onClickListener != null) {
                onClickListener!!.onClick(model, 1)
            }
        }

        holder.wishlist.setOnClickListener {
            if (onClickListener != null) {
                sessionManager = SessionManager(context)

                var buo = BranchUniversalObject()
                    .setCanonicalIdentifier(model.id)
                    .setContentMetadata(
                        ContentMetadata()
//                        .addCustomMetadata("custom_metadata_key1", "custom_metadata_val1")
//                        .addCustomMetadata("custom_metadata_key1", "custom_metadata_val1")
                            .addCustomMetadata("user_id", "${sessionManager.getUserId()}")
                            .setProductCategory(ProductCategory.HEALTH_AND_BEAUTY)
                            .setProductName(model.name)
                            .setProductCondition(ContentMetadata.CONDITION.EXCELLENT)
                            .setQuantity(1.0)
                            .setSku("test_sku")
                            .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
                    )

                   BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_WISHLIST)
                    .setCurrency(CurrencyType.INR)
                    .setDescription("Customer added item to wishlist")
                    .setShipping(0.0)
                    .setTax(18.0)
                    .setRevenue(model.price.toDouble())
                    .setSearchQuery("Test Search query")
                    .addContentItems(buo)
                    .logEvent(context)

                onClickListener!!.onClick(model, 2)
            }

        }


    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: CartEntity, id:Int)
    }


    override fun getItemCount(): Int {
        return itemsCart.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(binding: ItemCartTileBinding) : RecyclerView.ViewHolder(binding.root) {
        // Holds the TextView that will add each item to
        val itemName = binding.cartItemName
        val itemPrice = binding.cartItemPrice
        val itemImage = binding.cartItemImage
        val itemQuantity = binding.cartItemQuantity
        val remove = binding.remove
        val wishlist = binding.wishlist

    }

}