package org.brucknem.distractiontracker

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList as ArrayList

class RecyclerViewAdapter(
    private var images: ArrayList<String>,
    private var imageNames: ArrayList<String>,
    private var context: Context,
    private var onClickListener: OnClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    companion object {
        private const val TAG = "RecyclerViewAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(itemView = view, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called")

        Glide.with(context)
            .asBitmap()
            .load(images[position])
            .into(holder.image)

        holder.imageName.text = imageNames[position]
    }

    override fun getItemCount(): Int {
        return imageNames.size
    }

    class ViewHolder(
        itemView: View,
        private var onClickListener: OnClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {

        var image: CircleImageView = itemView.findViewById(R.id.image)
        var imageName: TextView = itemView.findViewById(R.id.image_name)
        var parentLayout: RelativeLayout = itemView.findViewById(R.id.parent_layout)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(absoluteAdapterPosition)
        }

    }

    interface OnClickListener {
        fun onClick(postition: Int)
    }
}
