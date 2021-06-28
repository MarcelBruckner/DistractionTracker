package org.brucknem.distractiontracker.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import de.hdodenhof.circleimageview.CircleImageView
import org.brucknem.distractiontracker.R
import org.brucknem.distractiontracker.data.Entry
import java.text.SimpleDateFormat

class RecyclerViewAdapter(
    private var entries: List<Entry>,
    private var context: Context,
    private var onClickListener: OnEntryClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val imageUrl = "https://cdn2.thecatapi.com/images/c2r.jpg"
    private var dateFormat: java.text.DateFormat = SimpleDateFormat.getDateTimeInstance();

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
            .load(imageUrl)
            .into(holder.image)

        val entry = entries[position]

        holder.entryId = entry.id
        holder.datetime.text = dateFormat.format(entry.datetime)
        holder.distraction.text = entry.distraction
        holder.howFeeling.text = entry.howFeeling
        holder.trigger.text = entry.triggerToString()
        holder.ideas.text = entry.ideas
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    class ViewHolder(
        itemView: View,
        private var onClickListener: OnEntryClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var entryId: Long = 0
        var image: CircleImageView = itemView.findViewById(R.id.image)
        var datetime: TextView = itemView.findViewById(R.id.datetime_listitem)
        var distraction: TextView = itemView.findViewById(R.id.distraction_listitem)
        var howFeeling: TextView = itemView.findViewById(R.id.how_feeling_listitem)
        var trigger: TextView = itemView.findViewById(R.id.trigger_listitem)
        var ideas: TextView = itemView.findViewById(R.id.ideas_listitem)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(entryId)
        }

    }

    interface OnEntryClickListener {
        fun onClick(entryId: Long)
    }
}
