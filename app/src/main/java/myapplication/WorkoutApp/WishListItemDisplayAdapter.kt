package myapplication.WorkoutApp


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import myapplication.WorkoutApp.databinding.ItemCartTileBinding
import myapplication.WorkoutApp.databinding.ItemWishlistTileBinding
import java.util.*

class WishListItemDisplayAdapter(val itemsWishList: ArrayList<WishListEntity>) :
    RecyclerView.Adapter<WishListItemDisplayAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWishlistTileBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model: WishListEntity = itemsWishList[position]

        holder.itemName.text = model.name
        holder.itemImage.setImageResource(model.image)

        holder.remove.setOnClickListener {
            Log.e("clicked", "$onClickListener")
            if (onClickListener != null) {
                onClickListener!!.onClick(model)
            }
        }



    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: WishListEntity)
    }


    override fun getItemCount(): Int {
        return itemsWishList.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(binding: ItemWishlistTileBinding) : RecyclerView.ViewHolder(binding.root) {
        // Holds the TextView that will add each item to
        val itemName = binding.wishListItemName
        val itemImage = binding.wishListItemImage
        val remove = binding.wishlistItemRemove

    }

}