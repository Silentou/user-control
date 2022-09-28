package com.megWorld.universal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.megWorld.universal.R
import com.megWorld.universal.entities.DataObject



class CategoryAdapter(private val catList: List<DataObject>)
    : RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {


    class CatViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryTitle:TextView = itemView.findViewById(R.id.catText)
        val categoryImage:ImageView = itemView.findViewById(R.id.catImage)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_category_menu,parent,false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cati = catList[position]
        holder.categoryImage.setImageResource(cati.Image)
        holder.categoryTitle.text = cati.Title
    }

    override fun getItemCount(): Int {
        return catList.size
    }



}