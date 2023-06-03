package myapplication.WorkoutApp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import myapplication.WorkoutApp.databinding.ItemCartTileBinding
import java.util.*

class CartItemDisplayAdapter(val itemsCart: ArrayList<CartEntity>) :
    RecyclerView.Adapter<CartItemDisplayAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartTileBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model: CartEntity = itemsCart[position]

        holder.itemName.text = model.name
        holder.itemPrice.text = model.price.toString()
        holder.itemImage.setImageResource(model.image)
        holder.itemQuantity.text = model.quantity.toString()

        holder.remove.setOnClickListener {
            Log.e("clicked", "$onClickListener")
            if (onClickListener != null) {
                onClickListener!!.onClick(model, 1)
            }
        }

        holder.wishlist.setOnClickListener {
            if (onClickListener != null) {
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