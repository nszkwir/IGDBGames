package com.spitzer.igdbgames.ui.pagination

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GamesPaginationScrollListener(
    private val layoutManager: LinearLayoutManager,
    val loadNextPageFunction: () -> Unit,
    val isLastPageFunction: () -> Boolean,
    val isLoadingFunction: () -> Boolean
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoadingFunction() && !isLastPageFunction()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadNextPageFunction()
            }
        }
    }

}
