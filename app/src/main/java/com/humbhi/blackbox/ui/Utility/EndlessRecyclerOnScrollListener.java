package com.humbhi.blackbox.ui.Utility;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (firstVisibleItem == totalItemCount - 1) {
                // End has been reached
                onLoadMore();
            };
        }

    public abstract void onLoadMore();
}