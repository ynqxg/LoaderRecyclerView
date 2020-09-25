package com.ynqxg.loaderRecyclerView

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ynqxg.loaderRecyclerView.listener.LoadDataListener
import com.ynqxg.loaderRecyclerView.utils.DensityUtil

/**
 * LoaderAdapter
 */
@Suppress("UNCHECKED_CAST")
abstract class LoaderAdapter<M, VH : LoaderViewHolder> :
    RecyclerView.Adapter<LoaderViewHolder>() {

    private val mDataList = mutableListOf<M>()

    private var mStatus = LoaderStatus.LOADING

    private var mPage = 1

    private val layoutParamsFull = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

    private val layoutParamsLine = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

    private var mLoadDataListener: LoadDataListener? = null

    fun setLoadDataListener(loadDataListener: LoadDataListener) {
        mLoadDataListener = loadDataListener
    }

    private val textViewStyle = { tv: TextView ->
        val margin = DensityUtil.dip2px(tv.context, 5f)
        val padding = DensityUtil.dip2px(tv.context, 10f)
        tv.layoutParams = layoutParamsLine.also {
            it.setMargins(0, margin, 0, margin)
        }
        tv.textSize = 16f
        tv.setTextColor(Color.parseColor("#333333"))
        tv.paint.isFakeBoldText = true
        tv.setPadding(0, padding, 0, padding)
        tv.gravity = Gravity.CENTER
    }

    fun addPage() {
        mPage = mPage.plus(1)
    }

    fun setPage(page: Int) {
        mPage = page
    }

    fun getPage(): Int {
        return mPage
    }

    private fun getDefaultView(context: Context): View? {
        return when (mStatus) {
            LoaderStatus.LOAD_FAIL -> {
                TextView(context).also {
                    textViewStyle(it)
                    it.text = "加载失败T^T"
                    it.setOnClickListener {
                        loadMoreCore()
                    }
                }
            }
            LoaderStatus.LOADING -> {
                loaderLoading(context)
            }
            LoaderStatus.LOAD_MORE -> {
                TextView(context).also {
                    textViewStyle(it)
                    it.text = "点击加载更多~"
                    it.setOnClickListener {
                        loadMoreCore()
                    }
                }
            }
            LoaderStatus.NOT_DATA -> {
                TextView(context).also {
                    textViewStyle(it)
                    it.text = "暂无相关数据"
                    it.setOnClickListener {
                        loadMoreCore()
                    }
                }
            }
            LoaderStatus.NOT_MORE -> {
                TextView(context).also {
                    textViewStyle(it)
                    it.text = "没有更多数据了~"
                    it.setOnClickListener {
                        loadMoreCore()
                    }
                }
            }
        }
    }


    private fun loaderLoading(context: Context?): View {
        return ProgressBar(context).also {
            val margin = DensityUtil.dip2px(it.context, 10f)
            it.layoutParams = layoutParamsLine.also { lp ->
                lp.setMargins(0, margin, 0, margin)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoaderViewHolder {
        return if (viewType == DEFAULT_VH) {
            LoaderViewHolder(LinearLayout(parent.context).apply {
                layoutParams = layoutParamsLine
                orientation = LinearLayout.VERTICAL
            })
        } else {
            initViewHolder(parent, LayoutInflater.from(parent.context))
        }
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, position: Int) {
        if (mDataList.size == position) {
            ((holder.itemView) as ViewGroup).removeAllViews()
            holder.itemView.addView(getDefaultView(holder.itemView.context))
        } else {
            bindDataAndView((holder as VH), getItem(position), position)
        }
    }

    private fun getItem(position: Int): M {
        return mDataList[position]
    }

    fun getListSize(): Int {
        return mDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mDataList.size) {
            DEFAULT_VH
        } else {
            USER_VH
        }
    }

    abstract fun initViewHolder(parent: ViewGroup, inflater: LayoutInflater): VH

    abstract fun bindDataAndView(holder: VH, item: M, position: Int)

    fun loadMoreCore() {
        if (mStatus != LoaderStatus.LOAD_MORE) return
        mStatus = LoaderStatus.LOADING
        applyChange()
        val listSize = mDataList.size
        loadData(listSize)
    }

    open fun loadData(size: Int) {
        mLoadDataListener?.load()
    }

    fun setStatus(status: LoaderStatus) {
        mStatus = status
    }

    fun addData(item: M) {
        mDataList.add(item)
    }

    fun addList(list: List<M>) {
        mDataList.addAll(list)
    }

    fun replaceList(list: List<M>) {
        mDataList.clear()
        mDataList.addAll(list)
    }

    fun clearList() {
        mDataList.clear()
    }

    override fun getItemCount(): Int {
        return mDataList.size.plus(1)
    }

    fun applyChange() {
        notifyDataSetChanged()
    }

    fun getStatus(): LoaderStatus {
        return mStatus
    }

    companion object {
        const val USER_VH = 0x01
        const val DEFAULT_VH = 0x09
        const val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT
        const val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    }
}