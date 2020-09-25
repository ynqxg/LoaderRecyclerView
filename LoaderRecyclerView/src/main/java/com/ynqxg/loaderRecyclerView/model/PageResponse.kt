package com.ynqxg.loaderRecyclerView.model

data class PageResponse<T>(
	var code: Int? = null,
	var msg: String? = null,
	var page: Page<T>? = null
)