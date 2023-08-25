package com.example.finals_mobdevt_group5.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finals_mobdevt_group5.DetailsActivity
import com.example.finals_mobdevt_group5.R
import com.example.finals_mobdevt_group5.databinding.CardViewBinding
import com.example.finals_mobdevt_group5.model.Recipe


class RecipeAdapter (private val userList:ArrayList<Recipe>, private val context: Context): RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {

    private lateinit var  mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    class MyViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val recipeName: TextView = itemView.findViewById(R.id.recipe_nameTxt)
        val publisherName: TextView = itemView.findViewById(R.id.publisherTxt)
        val cookTimeName: TextView = itemView.findViewById(R.id.cooktimeText)
        val cardFoodImageView: ImageView = itemView.findViewById(R.id.foodImage)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent,false)
        return MyViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cookTimeName.text =  userList[position].shortdescription
        holder.publisherName.text =  userList[position].email
        holder.recipeName.text =  userList[position].recipeName
        Glide.with(context).load(userList[position].url).into(holder.cardFoodImageView)

    }
}
