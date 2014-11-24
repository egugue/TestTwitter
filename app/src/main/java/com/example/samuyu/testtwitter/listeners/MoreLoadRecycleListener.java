package com.example.samuyu.testtwitter.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * Created by toyamaosamuyu on 2014/11/24.
 */
public abstract class MoreLoadRecycleListener extends RecyclerView.OnScrollListener {
    private static final String TAG = MoreLoadRecycleListener.class.getSimpleName();

    private RecyclerView recyclerView;
    private View footerView;
    private boolean isFinished = false;

    public MoreLoadRecycleListener(RecyclerView recyclerView, View footerView) {
        this.recyclerView = recyclerView;
        this.footerView = footerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (isEndOfList() && !isFinished) {
           onLoadMore();
        }
        if (dy < 0) {
            onUpScroll();
        }
        if (dy > 0)  {
            onDownScroll();
        }
    }

    public abstract void onLoadMore();
    public abstract void onUpScroll();
    public abstract void onDownScroll();

    public void finishLoad() {
       isFinished = true;
    }

    private boolean isEndOfList() {

        if ( recyclerView.getChildCount() == 0) {
            return false;
        }

        int totalCount = recyclerView.getAdapter().getItemCount() - 1;
        int  lastItemPositionOnScreen =  ( (LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        //Log.d("HOGE", totalCount + "   " + lastItemPositionOnScreen);
        if (totalCount != lastItemPositionOnScreen) {
            return false;
        }

        final int lastItemBottomPosition =  recyclerView.getChildAt( recyclerView.getChildCount()-1 ).getBottom();
        //Log.d("HOGE", lastItemBottomPosition+"  "+recyclerView.getHeight());
        if ( lastItemBottomPosition <= recyclerView.getHeight() ) {
           return true;
        }

        return false;
    }
}
