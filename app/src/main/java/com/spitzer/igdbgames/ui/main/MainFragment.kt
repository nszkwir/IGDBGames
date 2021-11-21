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

    private fun defineObservables() {

        viewModel.paginationViewState.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { viewState ->

                when (viewState) {

                    is MainViewState.LoadingInitialGameList -> {
                        if (viewState.isSuccess) {
                            paginationAdapter.setGameList(viewModel.gamesList.value!!)
                        } else {
                            // TODO load layout showing error
                        }
                    }

                    is MainViewState.RefreshingGameList -> {
                        finishRefreshing()
                        if (viewState.isSuccess) {
                            paginationAdapter.setGameList(viewModel.gamesList.value!!)
                        } else {
                            // TODO
                        }
                    }

                    is MainViewState.AddNextGamePageToList -> {
                        if (viewState.isSuccess) {
                            paginationAdapter.apply {
                                paginationAdapter.notifyGamesInserted(
                                    viewState.startingPosition,
                                    viewState.gamesAmountAdded
                                )
                                viewModel.nextPageLoaded()
                                hideFooter()
                            }
                        } else {
                            // TODO
                            // showFooterError()
                        }
                    }

                    is MainViewState.AddedAllGamesFromDb -> {
                        paginationAdapter.setGameList(viewModel.gamesList.value!!)
                        viewModel.nextPageLoaded()
                        hideFooter()
                    }
                }
            }
        })
    }

    private fun setupView() {

        binding.swipeContainer.setOnRefreshListener {
            hideFooter()
            viewModel.processViewEvent(MainViewEvent.PaginationEvent.RefreshGameListEvent())
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

        binding.footer.retryButton.setOnClickListener {
            //TODO define MainViewEvent.PaginationEvent.RetryFetchNextPage viewEvent and then handle it in the viewModel
        }

        paginationAdapter = GamesPaginationAdapter(
            itemClickFunction = { game -> onGameClicked(game) },
            primaryStarColor = resources.getColor(R.color.IGDBsoftViolet),
            secondaryStarColor = resources.getColor(R.color.IGDBsoftGray),
            drawableFallbackImage = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_no_image_24
            )!!
        )

        binding.gameListRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = paginationAdapter
            addOnScrollListener(
                GamesPaginationScrollListener(
                    layoutManager = linearLayoutManager,
                    loadNextPageFunction = { loadNextPage() },
                    isLastPageFunction = { viewModel.lastPageReached() },
                    isLoadingFunction = { viewModel.isLoading() },
                )
            )
        }
        paginationAdapter.setGameList(viewModel.gamesList.value ?: arrayListOf())
    }

    private fun hideFooter() {
        binding.footer.footerLayout.visibility = View.GONE
    }

    private fun loadNextPage() {
        binding.footer.footerLayout.visibility = View.VISIBLE
        viewModel.processViewEvent(MainViewEvent.PaginationEvent.FetchNextPageEvent())
    }

    private fun onGameClicked(game: Game) {
        viewModel.processViewEvent(MainViewEvent.GoToGameDetails(game.id))
    }

    private fun finishRefreshing() {
        viewModel.refreshFinished()
        binding.swipeContainer.post {
            binding.swipeContainer.isRefreshing = false
        }
    }
}
