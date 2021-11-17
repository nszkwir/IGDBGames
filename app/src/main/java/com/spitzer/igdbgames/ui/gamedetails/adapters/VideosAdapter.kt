package com.spitzer.igdbgames.ui.gamedetails.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.databinding.VideoItemBinding
import com.spitzer.igdbgames.extensions.listenToClick
import com.spitzer.igdbgames.repository.data.GameVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideosAdapter(
    private val videos: List<GameVideo>,
    private val drawableFallbackImage: Drawable,
    private val playDrawable: Drawable,
    private val onVideoClickFunction: (GameVideo) -> Unit
) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            videos[position],
            drawableFallbackImage,
            playDrawable
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val item =
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item).listenToClick { position, type ->
            onVideoClickFunction(videos[position])
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(
        private val itemBinding: VideoItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            video: GameVideo,
            drawableFallbackImage: Drawable,
            playDrawable: Drawable
        ) = CoroutineScope(Dispatchers.Main).launch {

            itemBinding.videoPreview.setImageResource(R.drawable.ic_baseline_hourglass_bottom_24)

            retriveVideoFrameFromVideo(
                //"http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4"
                "http://www.demonuts.com/Demonuts/smallvideo.mp4"
            ) { bitmap ->
                try {
                    if (bitmap == null) {
                        itemBinding.videoPreview.setImageDrawable(drawableFallbackImage)
                    } else {
                        itemBinding.videoPreview.setImageBitmap(bitmap)
                        itemBinding.videoPreview.foreground = playDrawable
                    }
                } catch (e: Exception) {
                    itemBinding.videoPreview.setImageDrawable(drawableFallbackImage)
                }
            }
        }
    }

    suspend fun retriveVideoFrameFromVideo(
        videoPath: String?,
        setBitmapFunction: (Bitmap?) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var bitmap: Bitmap? = null
            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            try {
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(
                    videoPath,
                    HashMap()
                )
                bitmap =
                    mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
            } finally {
                mediaMetadataRetriever?.release()
            }
            withContext(Dispatchers.Main) {
                setBitmapFunction(bitmap)
            }
        }
    }
}