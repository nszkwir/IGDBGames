package com.spitzer.igdbgames.ui.gamedetails.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.databinding.ScreenshotItemBinding
import com.spitzer.igdbgames.extensions.formatScreenshotImageUrl
import com.spitzer.igdbgames.repository.data.GameScreenshot
import com.squareup.picasso.Picasso

class ScreenshotsAdapter(
    private val screenshots: List<GameScreenshot>,
    private val drawableFallbackImage: Drawable
) : RecyclerView.Adapter<ScreenshotsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            screenshots[position],
            drawableFallbackImage
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val item =
            ScreenshotItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return screenshots.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(
        private val itemBinding: ScreenshotItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            screenshot: GameScreenshot,
            drawableFallbackImage: Drawable
        ) {
            try {
                Picasso.get()
                    .load(screenshot.url.formatScreenshotImageUrl())
                    .placeholder(R.drawable.igdb_cover)
                    .error(R.drawable.igdb_cover)
                    .into(itemBinding.screenshotImage)
            } catch (e: Exception) {
                itemBinding.screenshotImage.setImageDrawable(drawableFallbackImage)
            }
        }
    }
}
