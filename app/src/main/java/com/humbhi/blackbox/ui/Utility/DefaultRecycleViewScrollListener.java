package com.humbhi.blackbox.ui.Utility;

import androidx.recyclerview.widget.RecyclerView;

public class DefaultRecycleViewScrollListener extends RecyclerView.OnScrollListener {

    OnScrollStateListener onScrollStateListener;

    public DefaultRecycleViewScrollListener(OnScrollStateListener onScrollStateListener) {
        this.onScrollStateListener = onScrollStateListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (onScrollStateListener != null) {
            onScrollStateListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (onScrollStateListener != null) {
            onScrollStateListener.onScrolled(dx, dy);
        }
    }

    /**
     * Listener methods.
     */
    public interface OnScrollStateListener {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);
        void onScrolled(int dx, int dy);
    }
}
