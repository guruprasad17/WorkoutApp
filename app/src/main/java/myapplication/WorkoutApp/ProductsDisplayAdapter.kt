package myapplication.WorkoutApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import myapplication.WorkoutApp.databinding.ItemTileBinding
import java.util.*

    class ProductsDisplayAdapter (val products: ArrayList<ProductModel>) :
        RecyclerView.Adapter<ProductsDisplayAdapter.ViewHolder>() {


        private var onClickListener: OnClickListener? = null

        /**
         * Inflates the item view which is designed in xml layout file
         *
         * create a new
         * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemTileBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false
                )
            )
        }

        /**
         * Binds each item in the ArrayList to a view
         *
         * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
         * an item.
         *
         * This new ViewHolder should be constructed with a new View that can represent the items
         * of the given type. You can either create a new View manually or inflate it from an XML
         * layout file.
         */
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val model: ProductModel = products[position]

            holder.productName.text = model.getName().toString()
            holder.productPrice.text = model.getPrice().toString()
            holder.productImage.setImageResource(model.getImage())

            holder.itemView.setOnClickListener {
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
            fun onClick(position: ProductModel)
        }

//
//    fun setOnClickListener(onClickListener: View.OnClickListener) {
//        this.onClickListener = onClickListener
//    }

        // onClickListener Interface




        /**
         * Gets the number of items in the list
         */
        override fun getItemCount(): Int {
            return products.size
        }

        /**
         * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
         */
        class ViewHolder(binding: ItemTileBinding) : RecyclerView.ViewHolder(binding.root) {
            // Holds the TextView that will add each item to
            val productName = binding.productName
            val productPrice = binding.productPrice
            val productImage = binding.productImage
            val productTile = binding.productTile
        }
    }
