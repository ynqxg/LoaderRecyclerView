package com.ynqxg.loaderRecyclerView.listener

import com.ynqxg.loaderRecyclerView.model.PageResponse

abstract class SuccessListener<T> {

    abstract fun success(response: PageResponse<T>?)

}
