package com.ynqxg.loaderRecyclerView

import android.util.Log
import com.ynqxg.loaderRecyclerView.listener.FailureListener
import com.ynqxg.loaderRecyclerView.listener.SuccessListener
import com.ynqxg.loaderRecyclerView.model.PageResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class PageLoader<T>(
	private val adapter: LoaderAdapter<T, *>
) {

	private var mSuccessListener: SuccessListener<T>? = null
	private var mFailureListener: FailureListener? = null

	abstract fun getRequest(): Call<PageResponse<T>>

	fun setSuccessListener(listener: SuccessListener<T>) {
		mSuccessListener = listener
	}

	fun setFailureListener(listener: FailureListener) {
		mFailureListener = listener
	}

	fun load() {
		getRequest().enqueue(object : Callback<PageResponse<T>> {
			override fun onResponse(
				call: Call<PageResponse<T>>,
				response: Response<PageResponse<T>>
			) {
				if (response.body()?.page != null) {
					val page = response.body()?.page
					adapter.addPage()
					val current = page?.current
					val pages = page?.pages
					val records = page?.records
					var status = if (pages == current) {
						LoaderStatus.NOT_MORE
					} else {
						LoaderStatus.LOAD_MORE
					}
					if (records != null && records.isNotEmpty()) {
						adapter.addList(records)
					} else {
						status = LoaderStatus.NOT_DATA
					}
					adapter.setStatus(status)
					adapter.applyChange()
				} else {
					adapter.setStatus(LoaderStatus.NOT_DATA)
					adapter.applyChange()
				}
				mSuccessListener?.success(response.body())
			}

			override fun onFailure(call: Call<PageResponse<T>>, t: Throwable) {
				Log.e("TAG", "onFailure: ${t.message}", t)
				adapter.setStatus(LoaderStatus.LOAD_FAIL)
				mFailureListener?.failure(t)
			}

		})
    }

    companion object {
        fun toBody(json: String): RequestBody {
            return RequestBody.create(
				MediaType.parse("application/json; charset=utf-8"),
				json
			)
        }
    }

}