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
    private var context: Context,
    private var entries: List<Entry>,
    private var onClickListener: OnEntryClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val imageUrl = "https://cdn2.thecatapi.com/images/c2r.jpg"
    private var dateFormat: java.text.DateFormat = SimpleDateFormat.getDateTimeInstance();
    private var entriesToShow: EntriesToShow = EntriesToShow.ALL
    private var ascending = true

    enum class EntriesToShow {
        ALL,
        INTERNAL_TRIGGER,
        EXTERNAL_TRIGGER,
        PLANNING_PROBLEM
    }

    init {
        toggleSort(true)
    }

    companion object {
        private const val TAG = "RecyclerViewAdapter"
    }

    fun toggleSort(ascending: Boolean? = null) {
        this.ascending = ascending ?: !this.ascending

        if (entries.isEmpty()) {
            return
        }

        val sorted: ArrayList<Entry> = ArrayList(entries.sortedBy { it.datetime })
        if (this.ascending) {
            sorted.reverse()
        }
        entries = sorted
        notifyDataSetChanged()
    }

    fun setEntriesToShow(entriesToShow: EntriesToShow) {
        this.entriesToShow = entriesToShow
        notifyDataSetChanged()
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

        holder.hide()
        when (entriesToShow) {
            EntriesToShow.ALL -> holder.show()
            EntriesToShow.INTERNAL_TRIGGER -> if (entry.internalTrigger) holder.show()
            EntriesToShow.EXTERNAL_TRIGGER -> if (entry.externalTrigger) holder.show()
            EntriesToShow.PLANNING_PROBLEM -> if (entry.planningProblem) holder.show()
        }

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

        fun hide() {
            itemView.visibility = View.GONE
            itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }

        fun show() {
            itemView.visibility = View.VISIBLE
            itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(entryId)
        }

    }

    interface OnEntryClickListener {
        fun onClick(entryId: Long)
    }
}
