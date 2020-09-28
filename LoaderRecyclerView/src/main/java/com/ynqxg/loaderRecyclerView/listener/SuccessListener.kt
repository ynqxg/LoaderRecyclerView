package com.ynqxg.loaderRecyclerView.listener

import com.ynqxg.loaderRecyclerView.model.PageResponse

interface SuccessListener<T> {

    fun success(response: PageResponse<T>?)

    fun success(response: ((PageResponse<T>) -> Unit)?)
}
