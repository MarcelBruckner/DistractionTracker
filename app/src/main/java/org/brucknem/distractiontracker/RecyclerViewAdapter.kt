package org.brucknem.distractiontracker

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList as ArrayList

class RecyclerViewAdapter(
    private var entries: ArrayList<Entry>,
    private var context: Context,
    private var onClickListener: OnClickListener
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

        holder.datetime.text = dateFormat.format(entry.datetime)
        holder.distraction.text = entry.distraction
        holder.howFeeling.text = entry.howFeeling
        holder.trigger.text = if (entry.internal) "Internal" else "External"
        holder.planningProblem.text = entry.planningProblem
        holder.ideas.text = entry.ideas
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    class ViewHolder(
        itemView: View,
        private var onClickListener: OnClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var image: CircleImageView = itemView.findViewById(R.id.image)
        var datetime: TextView = itemView.findViewById(R.id.datetime_listitem)
        var distraction: TextView = itemView.findViewById(R.id.distraction_listitem)
        var howFeeling: TextView = itemView.findViewById(R.id.how_feeling_listitem)
        var trigger: TextView = itemView.findViewById(R.id.trigger_listitem)
        var planningProblem: TextView = itemView.findViewById(R.id.planning_problem_listitem)
        var ideas: TextView = itemView.findViewById(R.id.ideas_listitem)

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
