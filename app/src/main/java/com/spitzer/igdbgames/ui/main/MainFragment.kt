package com.spitzer.igdbgames.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseFragment
import com.spitzer.igdbgames.databinding.MainFragmentBinding
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.ui.pagination.GamesPaginationAdapter
import com.spitzer.igdbgames.ui.pagination.GamesPaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var paginationAdapter: GamesPaginationAdapter

    private val viewModel: MainViewModel by viewModels()
    override fun obtainViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        defineObservables()
        setupView()
        return binding.root
    }

    // Observing viewModels STATES
    private fun defineObservables() {

        viewModel.gamesAddedState.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { gamesAdded ->
                if (gamesAdded) {
                    paginationAdapter.apply {
                        removeLoadingFooter()
                        viewModel.isLoadingNextPage = false
                        addAll(viewModel.addedGames.value!!)
                    }
                } else {
                    paginationAdapter.showErrorLoading()
                }
            }
        })

        viewModel.gamesLoadingState.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { gamesRefreshed ->
                finishRefreshing()
                if (gamesRefreshed) {
                    paginationAdapter.apply {
                        setGameList(viewModel.loadedGames.value!!)
                    }
                } else {
                    paginationAdapter.removeLoadingFooter()
                }
            }
        })

        viewModel.lastPageReachedState.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { isLastPage ->
                viewModel.lastPageReached = isLastPage
            }
        })
    }

    private fun setupView() {

        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshGamesList()
        }

        binding.swipeContainer.setColorSchemeResources(
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
        )

        val linearLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        paginationAdapter = GamesPaginationAdapter(
            itemClickFunction = { game -> onGameClicked(game) },
            primaryStarColor = resources.getColor(R.color.IGDBsoftViolet),
            secondaryStarColor = resources.getColor(R.color.IGDBsoftGray),
            drawableFallbackImage = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_no_image_24
            )!!,
            retryFunction = { Unit },
            onFinishRefresh = { Unit }
        )
        binding.gameListRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = paginationAdapter
            addOnScrollListener(
                GamesPaginationScrollListener(
                    layoutManager = linearLayoutManager,
                    loadNextPageFunction = { loadNextPage() },
                    isLastPageFunction = { viewModel.lastPageReached },
                    isLoadingFunction = { viewModel.isLoadingNextPage }
                    )
            )
        }
        paginationAdapter.setGameList(viewModel.loadedGames.value?: arrayListOf())
    }

    private fun loadNextPage() {
        paginationAdapter.addLoadingFooter()
        viewModel.fetchNextPage()
    }

    private fun onGameClicked(game: Game) {
        viewModel.onGameClicked(game)
    }

    private fun finishRefreshing() {
        binding.swipeContainer.post {
            binding.swipeContainer.isRefreshing = false
        }
    }
}
