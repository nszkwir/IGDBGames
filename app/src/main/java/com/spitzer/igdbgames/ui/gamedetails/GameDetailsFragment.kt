package com.spitzer.igdbgames.ui.gamedetails

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseFragment
import com.spitzer.igdbgames.databinding.GameDetailsFragmentBinding
import com.spitzer.igdbgames.extensions.formatCoverImageUrl
import com.spitzer.igdbgames.extensions.formatScreenshotBackgroundImageUrl
import com.spitzer.igdbgames.extensions.round
import com.spitzer.igdbgames.extensions.setStarsProgressColor
import com.spitzer.igdbgames.repository.data.getPlatformsNames
import com.spitzer.igdbgames.repository.data.getReleaseDate
import com.spitzer.igdbgames.ui.gamedetails.adapters.ScreenshotsAdapter
import com.spitzer.igdbgames.ui.gamedetails.adapters.VideosAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameDetailsFragment : BaseFragment() {

    private var _binding: GameDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameDetailsViewModel by viewModels()
    override fun obtainViewModel() = viewModel

    private lateinit var screenshotsAdapter: ScreenshotsAdapter
    private lateinit var videosAdapter: VideosAdapter

    private val args: GameDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameDetailsFragmentBinding.inflate(inflater, container, false)
        defineObservables()
        viewModel.loadGameData(args.gameId)
        return binding.root
    }

    private fun defineObservables() {
        viewModel.dataLoaded.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { isDataLoaded ->
                if (isDataLoaded) {
                    setupView()
                } else {
                    //TODO show error layout or message
                }
                viewModel.setLoading(false)
            }
        })

        viewModel.localRatingUpdated.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { localRatingUpdated ->
                if (localRatingUpdated) {
                    Toast.makeText(
                        requireContext(),
                        "Rating updated locally",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Could not save rating locally",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupView() {
        with(viewModel.game.value!!) {

            screenshotsAdapter = ScreenshotsAdapter(
                this.screenshots!!,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_no_image_24)!!
            )

            binding.screenshotsRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                adapter = screenshotsAdapter
            }

            videosAdapter = VideosAdapter(
                this.videos!!,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_no_image_24)!!,
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_play_circle_filled_12
                )!!
            ) { gameVideo ->
                viewModel.navigateToVideoFragment(gameVideo)
            }

            binding.videosRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                adapter = videosAdapter
            }

            binding.title.text = this.name
            binding.date.text = this.getReleaseDate()

            val stars = binding.ratingBar.progressDrawable as LayerDrawable
            stars.setStarsProgressColor(
                resources.getColor(R.color.IGDBsoftViolet),
                resources.getColor(R.color.IGDBsoftGray)
            )
            binding.ratingBar.progressDrawable = stars
            binding.ratingBar.rating = this.rating?.toFloat() ?: 0.0f

            try {
                Picasso.get()
                    .load(this.cover!!.url.formatCoverImageUrl())
                    .placeholder(R.drawable.igdb_cover)
                    .error(R.drawable.igdb_cover)
                    .into(binding.gameCover)
            } catch (e: Exception) {
                binding.gameCover.setImageDrawable(resources.getDrawable(R.drawable.ic_no_image_24))
            }

            try {
                Picasso.get()
                    .load(this.screenshots.first().url.formatScreenshotBackgroundImageUrl())
                    .placeholder(R.drawable.igdb_app_background)
                    .error(R.drawable.igdb_app_background)
                    .into(binding.headerBackground)
            } catch (e: Exception) {
                binding.headerBackground.setImageDrawable(resources.getDrawable(R.drawable.ic_no_image_24))
            }

            binding.platforms.text = this.getPlatformsNames()

            binding.summary.text = this.summary
            binding.storyline.text = this.storyline

            binding.linkToWeb.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this.url))
                startActivity(browserIntent)
            }
        }


        val stars = binding.myRatingBar.progressDrawable as LayerDrawable
        stars.setStarsProgressColor(
            resources.getColor(R.color.myRatingBarColor),
            resources.getColor(R.color.IGDBsoftGray)
        )
        binding.myRatingBar.progressDrawable = stars

        if (viewModel.rating.value != null) {
            binding.myRatingText.text = "My rating: ${viewModel.rating.value!!.rating?.round(2)}"
            binding.myRatingBar.rating = viewModel.rating.value!!.rating?.toFloat() ?: 0.0f
            binding.myRatingBar.visibility = View.VISIBLE
        } else {
            binding.myRatingText.text = "Click to rate this game!"
            binding.myRatingBar.visibility = View.GONE
        }

        binding.myRatingLayout.setOnClickListener {
            val dialog = RatingDialog(
                viewModel.rating.value?.rating?.toFloat()?:null
            ) { newRatingValue ->
                onRatingSet(newRatingValue)
            }
            dialog.show(parentFragmentManager, "Game Rating")
        }

    }

    private fun onRatingSet(newRatingValue: Float?) {
        if (newRatingValue != null) {
            binding.myRatingBar.rating = newRatingValue
            binding.myRatingText.text = "My rating: ${newRatingValue.round(2)}"
            binding.myRatingBar.visibility = View.VISIBLE
            viewModel.updateLocalRating(newRatingValue)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
