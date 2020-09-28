package com.ynqxg.loaderRecyclerView.listener

interface FailureListener {
    fun failure(t: Throwable) {}
    fun failure(t: (Throwable) -> Unit) {}
}
