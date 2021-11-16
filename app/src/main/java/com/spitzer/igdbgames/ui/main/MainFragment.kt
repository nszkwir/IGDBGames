package com.spitzer.igdbgames.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseFragment
import com.spitzer.igdbgames.databinding.MainFragmentBinding
import com.spitzer.igdbgames.repository.data.Game
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
//    private lateinit var pagingAdapter: GameListPagingAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun obtainViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {

        binding.swipeContainer.setOnRefreshListener {
            //pagingAdapter.refreshList()
        }

        binding.swipeContainer.setColorSchemeResources(
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
            R.color.IGDBstrongViolet,
        )

//        pagingAdapter = GameListPagingAdapter(
//            { game -> onGameClicked(game) },
//            { finishRefreshing() },
//            resources.getColor(R.color.IGDBsoftViolet),
//            resources.getColor(R.color.IGDBsoftGray),
//            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_no_image_24)!!
//        )
//        pagingAdapter.addLoadStateListener { loadState ->
//            viewModel.manageLoadingStates(loadState)
//        }
//        binding.gameListRecyclerView.apply {
//            layoutManager =
//                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//            adapter = pagingAdapter.withLoadStateFooter(
//                footer = FooterLoaderAdapter { pagingAdapter.retry() }
//            )
//
//        }
//        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
//            viewModel.gameList.collectLatest { pagingData ->
//                pagingAdapter.submitData(pagingData)
//            }
//        }
    }

    private fun onGameClicked(game: Game) {
        //viewModel.onGameClicked(game)
    }

    private fun finishRefreshing() {
        binding.swipeContainer.isRefreshing = false
    }
}
