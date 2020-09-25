package com.ynqxg.loaderRecyclerView.model

data class Page<T>(
	var current: Int? = null,
	var hitCount: Boolean? = null,
	var optimizeCountSql: Boolean? = null,
	var orders: List<Any>? = null,
	var pages: Int? = null,
	var records: List<T>? = null,
	var searchCount: Boolean? = null,
	var size: Int? = null,
	var total: Int? = null
)