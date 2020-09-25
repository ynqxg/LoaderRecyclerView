package com.ynqxg.loaderRecyclerView.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class LoaderScrollListener : RecyclerView.OnScrollListener() {

    //用来标记是否正在向最后一个滑动
    private var isSlidingToLast = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState);
        val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        // 当不滚动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //获取最后一个完全显示的ItemPosition
            val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
            val totalItemCount = manager.itemCount

            // 判断是否滚动到底部，并且是向右滚动
            if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                //加载更多功能的代码
                onScrollToBottom()
            }
        }
    }

    abstract fun onScrollToBottom()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        isSlidingToLast = dx > 0
    }
}