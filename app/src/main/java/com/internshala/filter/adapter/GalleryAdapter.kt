package com.internshala.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.filter.R
import com.internshala.filter.model.ImageEntity
import com.internshala.filter.utility.ImageConverter

class GalleryAdapter(private val galleryList: List<ImageEntity> , private val context: Context) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val photoView =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item_view, parent, false)
        return ViewHolder(photoView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val converter = ImageConverter(context)
        holder.image.setImageBitmap(converter.toBitmap(galleryList[position].image))
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }
}