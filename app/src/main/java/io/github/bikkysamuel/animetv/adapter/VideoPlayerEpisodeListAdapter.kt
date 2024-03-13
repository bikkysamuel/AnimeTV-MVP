package io.github.bikkysamuel.animetv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.bikkysamuel.animetv.R
import io.github.bikkysamuel.animetv.app.MyApp
import io.github.bikkysamuel.animetv.listeners.LoadSelectedVideoListener
import io.github.bikkysamuel.animetv.listeners.fragments.VideoPlayerFragmentListener
import io.github.bikkysamuel.animetv.models.VideoPlayerPageData
import io.github.bikkysamuel.animetv.utils.MyLogger
import io.github.bikkysamuel.animetv.utils.MyUtils


class VideoPlayerEpisodeListAdapter(
    private val videoPlayerPageData: VideoPlayerPageData, private val videoTitle: String,
    private var addedAsFavourite: Boolean, private val loadSelectedVideoListener: LoadSelectedVideoListener,
    private val videoPlayerFragmentListener: VideoPlayerFragmentListener
    ):
    RecyclerView.Adapter<VideoPlayerEpisodeListAdapter.Companion.EpisodeViewHolder>() {

    private lateinit var btnAddToFav: Button

    companion object {
        class EpisodeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var cardViewVideoPlayerPageItem: CardView
            var conLytVideoPlayerPageFirstItem: ConstraintLayout
            var conLytVideoPlayerEpisodeItem: ConstraintLayout
            var btnAddToFav: Button
            var txtAnimeTitle: TextView
            var txtVideoTitle: TextView
            var txtAnimeDescription: TextView
            var imgAnimePoster: ImageView
            var txtVideoType: TextView
            var txtSENumber: TextView
            var txtUploadDate: TextView

            init {
                cardViewVideoPlayerPageItem =
                    itemView.findViewById<CardView>(R.id.cardView_videoPlayerPageItem)

                conLytVideoPlayerPageFirstItem =
                    itemView.findViewById<ConstraintLayout>(R.id.conLyt_videoPlayerPageFirstItem)
                conLytVideoPlayerEpisodeItem =
                    itemView.findViewById<ConstraintLayout>(R.id.conLyt_videoPlayerPageEpisode)

                btnAddToFav = itemView.findViewById(R.id.btn_addToFav)

                txtAnimeTitle = itemView.findViewById<TextView>(R.id.txt_videoPlayerPageTitle)
                txtVideoTitle = itemView.findViewById<TextView>(R.id.txt_currentVideoTitle)
                txtAnimeDescription =
                    itemView.findViewById<TextView>(R.id.txt_videoPlayerPageDescription)
                imgAnimePoster = itemView.findViewById<ImageView>(R.id.img_videoPlayerPagePoster)
                txtVideoType = itemView.findViewById<TextView>(R.id.txt_videoType)
                txtSENumber = itemView.findViewById<TextView>(R.id.txt_seNumber)
                txtUploadDate = itemView.findViewById<TextView>(R.id.txt_uploadDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_anime_video_player_page_item, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoPlayerPageData.videoPlayerEpisodeItems.size + 1
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.conLytVideoPlayerPageFirstItem.visibility = if (position == 0) View.VISIBLE else View.GONE
        holder.conLytVideoPlayerEpisodeItem.visibility = if (position == 0) View.GONE else View.VISIBLE

        if (position == 0) {
            holder.btnAddToFav.text = if (addedAsFavourite) MyApp.instance.getText(R.string.btn_fav) else MyApp.instance.getString(R.string.btn_add_as_fav)
            holder.txtAnimeTitle.text = videoPlayerPageData.animeTitle
            holder.txtVideoTitle.text = videoTitle
            holder.txtAnimeDescription.text = videoPlayerPageData.animeDescription
            Glide.with(holder.imgAnimePoster)
                .load(videoPlayerPageData.videoPlayerEpisodeItems[0].imgUrl)
                .into(holder.imgAnimePoster)

            holder.btnAddToFav.setOnClickListener {
                videoPlayerFragmentListener.updateFavouriteItemBtnClicked()
            }
            this.btnAddToFav = holder.btnAddToFav
        } else {
            val videoPlayerEpisodeItem = videoPlayerPageData.videoPlayerEpisodeItems[position - 1]
            holder.txtVideoType.text = videoPlayerEpisodeItem.videoType.name
//            holder.txtSENumber.text = "S " + videoPlayerEpisodeItem.season + " - E " + videoPlayerEpisodeItem.episode
            holder.txtSENumber.text = "Ep # " + videoPlayerEpisodeItem.episode
            holder.txtUploadDate.text = videoPlayerEpisodeItem.uploadDate
            holder.conLytVideoPlayerEpisodeItem.setOnClickListener(View.OnClickListener {
                loadSelectedVideoListener.loadSelectedVideoPage(videoPlayerEpisodeItem)
            })
        }
    }

    fun updateFavouriteValue(addedAsFavourite: Boolean) {
        this.addedAsFavourite = addedAsFavourite
        updateBtnAddToFavText()
    }

    private fun updateBtnAddToFavText() {
        try {
            this.btnAddToFav.text = if (this.addedAsFavourite) MyApp.instance.getString(R.string.btn_fav) else MyApp.instance.getString(R.string.btn_add_as_fav)
        } catch (e: Exception) {
            MyLogger.d(this, "updateBtnAddToFavText() -> Error: ${e.message}")
        }
    }
}