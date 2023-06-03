package myapplication.WorkoutApp

import java.util.*

object products {

    fun defaultProductList(): ArrayList<ProductModel> {

        val productsList = ArrayList<ProductModel>()

        val product1 = ProductModel(1 , "mass gainer protein 500g", R.drawable.product_1, "1000" )
        productsList.add(product1 )
        val product2 = ProductModel(2, "pro hydro stamina gainer", R.drawable.product_2, "1536" )
        productsList.add(product2)
        val product3 = ProductModel(3 , "real mass gainer 2 kg", R.drawable.product_3, "2634" )
        productsList.add(product3)
        val product4 = ProductModel(4 , "protein bar", R.drawable.product_4, "342" )
        productsList.add(product4)
        val product5 = ProductModel(5 , "protein chocolate", R.drawable.product_5, "442" )
        productsList.add(product5)
        val product6 = ProductModel(6 , "bottle for protein shakes", R.drawable.product_6, "144")
        productsList.add(product6)

        return productsList


    }
}