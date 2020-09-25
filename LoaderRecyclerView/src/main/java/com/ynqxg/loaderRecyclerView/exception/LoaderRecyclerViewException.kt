package com.ynqxg.loaderRecyclerView.exception

class LoaderRecyclerViewException(override val message: String?, override val cause: Throwable?) :
    Throwable(message, cause) {
    constructor() : this("LoaderRecyclerViewException", null)
    constructor(message: String?) : this(message, null)

    constructor(cause: Throwable?) : this(cause?.toString(), cause)
}
