package com.megWorld.universal.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.megWorld.universal.R
import com.megWorld.universal.entities.ProductObject
import kotlinx.android.synthetic.main.product_list.view.*


class ProductAdapter(private val productList: List<ProductObject>, val context: Context)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {



    inner class ProductViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)
      //  fun bind(productObject: ProductObject){
//      var aProductImage:ImageView = itemView.findViewById(R.id.productImage)
//            var aProductTitle:TextView = itemView.findViewById(R.id.productName)
//            var aProductQuantity:TextView = itemView.findViewById(R.id.productQuantity)
//            var aProductPrize:TextView = itemView.findViewById(R.id.productPrice)


        //}



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.product_list,parent,false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
       val pList= productList[position]
        holder.itemView.apply {
            Glide.with(this).load(pList.acf.image).into(productImage)
            productName.text = pList.acf.title
            productQuantity.text = pList.acf.quantity
            productPrice.text = pList.acf.price
        }
//        holder.aProductImage.setImageResource(pList.acf.image.toInt())
//        holder.aProductTitle.text = pList.acf.title
//        holder.aProductQuantity.text = pList.acf.quantity
//        holder.aProductPrize.text = pList.acf.price

//        holder.aProductImage.setImageResource(prod.productImage)
//        holder.aProductTitle.text = prod.productTitle
//        holder.aProductQuantity.text = prod.productQuantity
//        holder.aProductPrize.text = prod.productPrice

    }

    override fun getItemCount(): Int {
        return productList.size
    }



}