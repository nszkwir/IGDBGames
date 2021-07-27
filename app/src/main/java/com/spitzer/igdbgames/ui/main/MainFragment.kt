package com.spitzer.igdbgames.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseFragment
import com.spitzer.igdbgames.databinding.MainFragmentBinding
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.ui.main.paging.FooterLoaderAdapter
import com.spitzer.igdbgames.ui.main.paging.GameListPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagingAdapter: GameListPagingAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun obtainViewModel() = viewModel

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    @ExperimentalPagingApi
    private fun setupView() {

        binding.swipeContainer.setOnRefreshListener {
            pagingAdapter.refreshList()
        }

        binding.swipeContainer.setColorSchemeResources(
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
        )

        pagingAdapter = GameListPagingAdapter(
            { game -> onGameClicked(game) },
            { finishRefreshing() },
            resources.getColor(R.color.IGDBsoftViolet),
            resources.getColor(R.color.IGDBsoftGray),
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_no_image_24)!!
        )
        pagingAdapter.addLoadStateListener { loadState ->
            viewModel.manageLoadingStates(loadState)
        }
        binding.gameListRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = pagingAdapter.withLoadStateFooter(
                footer = FooterLoaderAdapter { pagingAdapter.retry() }
            )

        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.gameList.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun onGameClicked(game: Game) {
        viewModel.onGameClicked(game)
    }

    private fun finishRefreshing() {
        binding.swipeContainer.isRefreshing = false
    }
}
