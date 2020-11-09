package com.sirius.net.tlink.adapters

import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.activities.Main.MainActivity

class WalkThroughAdapter(var preferences:SharedPreferences): RecyclerView.Adapter<WalkThroughAdapter.SliderHolder>() {


    override fun getItemCount(): Int = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderHolder {
        val inflater= LayoutInflater.from(parent.context)
        return SliderHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: SliderHolder, position: Int) {
        val context =holder.itemView.context

        //Assign the images
        when(position){
            0 ->{
                holder.stepImage.setImageResource(R.drawable.ic_humanlike)
                holder.titleText.text = context.getString(R.string.title_walk1)
                holder.descriptionText.text = context.getString(R.string.walk_pos1)
            }
            1 ->{
                holder.stepImage.setImageResource(R.drawable.ic_humanlike)
                holder.titleText.text = context.getString(R.string.title_walk2)
                holder.descriptionText.text = context.getString(R.string.walk_pos2)
            }
            2 ->{
                holder.stepImage.setImageResource(R.drawable.ic_humanlike)
                holder.titleText.text = context.getString(R.string.title_walkpos3)
                holder.descriptionText.text = context.getString(R.string.description_pos3)
            }
            3 ->{
                holder.stepImage.setImageResource(R.drawable.ic_humanlike)
                holder.titleText.text = context.getString(R.string.title_walk_pos4)
                holder.descriptionText.text = context.getString(R.string.description_pos4)
            }
            4 ->{
                holder.stepImage.setImageResource(R.drawable.ic_humanlike)
                holder.descriptionText.text = context.getString(R.string.description_pos5)
                holder.titleText.visibility = INVISIBLE
            }
        }
    }

    class SliderHolder(inflater:LayoutInflater,parent:ViewGroup)
        :RecyclerView.ViewHolder(inflater.inflate(R.layout.walk_through_item,parent,false)) {

        val stepImage:ImageView = itemView.findViewById(R.id.step_image)
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val descriptionText:TextView= itemView.findViewById(R.id.description_text)
    }
}