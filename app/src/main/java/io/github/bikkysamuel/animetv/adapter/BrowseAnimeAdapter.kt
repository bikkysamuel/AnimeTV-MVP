package io.github.bikkysamuel.animetv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import io.github.bikkysamuel.animetv.R
import io.github.bikkysamuel.animetv.listeners.adapters.BrowseAnimeAdapterCallbackListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

class BrowseAnimeAdapter(private var browseAnimeItems: ArrayList<BrowseAnimeItem>, private val browseAnimeAdapterCallbackListener: BrowseAnimeAdapterCallbackListener) :
    RecyclerView.Adapter<BrowseAnimeAdapter.Companion.BrowseAnimeVideHolder>() {

    companion object {
        class BrowseAnimeVideHolder(itemView: View) : ViewHolder(itemView) {

            var imgPoster: ImageView
            var txtAnimeTitle: TextView
            var txtAnimeUploadDate: TextView

            init {
                imgPoster = itemView.findViewById(R.id.img_animePoster)
                txtAnimeTitle = itemView.findViewById(R.id.txt_animeTitle)
                txtAnimeUploadDate = itemView.findViewById(R.id.txt_uploadDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowseAnimeVideHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.browse_anime_list_item, parent, false)
        return BrowseAnimeVideHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return browseAnimeItems.size
    }

    override fun onBindViewHolder(viewHolder: BrowseAnimeVideHolder, position: Int) {
        val browseAnimeItem: BrowseAnimeItem = browseAnimeItems[position]
        Glide.with(viewHolder.itemView.context)
            .load(browseAnimeItem.imgUrl)
            .into(viewHolder.imgPoster)

        viewHolder.txtAnimeTitle.text = browseAnimeItem.name
        viewHolder.txtAnimeUploadDate.text = browseAnimeItem.uploadedDate

        viewHolder.itemView.setOnClickListener {
            browseAnimeAdapterCallbackListener.onAnimeItemClick(browseAnimeItem)
        }
    }
}