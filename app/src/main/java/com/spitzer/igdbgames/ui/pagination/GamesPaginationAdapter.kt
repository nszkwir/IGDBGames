package com.spitzer.igdbgames.ui.pagination

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.databinding.GameItemBinding

import com.spitzer.igdbgames.extensions.formatCoverImageUrl
import com.spitzer.igdbgames.extensions.setStarsProgressColor
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.data.getPlatformsNames
import com.spitzer.igdbgames.repository.data.getReleaseDate
import com.squareup.picasso.Picasso

class GamesPaginationAdapter(
    private val itemClickFunction: (Game) -> Unit,
    private val onFinishRefresh: () -> Unit,
    private val primaryStarColor: Int,
    private val secondaryStarColor: Int,
    private val drawableFallbackImage: Drawable,
    private val retryFunction: () -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var gameList: MutableList<Game> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setGameList(pGameList: ArrayList<Game>) {
        gameList = pGameList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return GameItemViewHolder(
            GameItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClickFunction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        getItem(position).let {
            (holder as GameItemViewHolder).bind(
                it,
                primaryStarColor,
                secondaryStarColor,
                drawableFallbackImage
            )
        }
    }

    override fun getItemCount() = gameList.size

    fun notifyGamesInserted(position: Int, size: Int) {
        notifyItemRangeInserted(position, size)
    }

    private fun getItem(position: Int) = gameList[position]

    inner class GameItemViewHolder(
        private val itemBinding: GameItemBinding,
        private val clickFunction: (Game) -> Unit,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            game: Game,
            primaryStarColor: Int,
            secondaryStarColor: Int,
            drawableFallbackImage: Drawable
        ) {
            itemBinding.ratingBar.apply {
                val stars = itemBinding.ratingBar.progressDrawable as LayerDrawable
                stars.setStarsProgressColor(primaryStarColor, secondaryStarColor)
                progressDrawable = stars
            }

            try {
                Picasso.get()
                    .load(game.cover?.url.formatCoverImageUrl())
                    .placeholder(R.drawable.igdb_cover)
                    .error(R.drawable.igdb_cover)
                    .into(itemBinding.gameCover)
            } catch (e: Exception) {
                itemBinding.gameCover.setImageDrawable(drawableFallbackImage)
            }

            game.name?.let {
                itemBinding.title.text = game.name
            } ?: run {
                itemBinding.title.text = DEFAULT_TITLE_STRING
            }

            game.releaseDate?.let {
                itemBinding.date.text = game.getReleaseDate()
            } ?: run {
                itemBinding.date.visibility = INVISIBLE
            }

            game.platforms?.let {
                val platformString = game.getPlatformsNames()
                if (platformString.isEmpty()) {
                    itemBinding.platform.visibility = INVISIBLE
                } else {
                    itemBinding.platform.text = platformString
                }
            } ?: run {
                itemBinding.platform.visibility = INVISIBLE
            }

            game.rating?.let {
                itemBinding.ratingBar.rating = (game.rating / 20).toFloat()
            } ?: run {
                itemBinding.ratingBar.rating = 0.0f
            }

            this.itemView.setOnClickListener {
                clickFunction(game)
            }
        }
    }

    companion object {
        private const val DEFAULT_TITLE_STRING = ""
    }
}
