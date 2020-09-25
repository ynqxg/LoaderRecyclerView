package com.ynqxg.loaderRecyclerView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ynqxg.loaderRecyclerView.listener.LoaderScrollListener

/**
 * LoaderListView
 */
class LoaderRecyclerView(context: Context, attributeSet: AttributeSet?) :
    RecyclerView(context, attributeSet) {

    private var mAutoLoad = false

    fun setIsAutoLoad(isAuto: Boolean) {
        mAutoLoad = isAuto
    }

    constructor(context: Context) : this(context, null)

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (mAutoLoad) {
            addOnScrollListener(object : LoaderScrollListener() {
                override fun onScrollToBottom() {
                    val loaderAdapter = (adapter as LoaderAdapter<*, *>)
                    val size = loaderAdapter.getListSize()
                    if (size > 0) {
                        loaderAdapter.loadMoreCore()
                    }
                }
            })
        }
    }

}