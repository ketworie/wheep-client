package com.ketworie.wheep.client

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import java.util.concurrent.Executor

class ListDataSource<T>(list: List<T>) : PositionalDataSource<T>() {
    private val mList: List<T> = ArrayList(list)

    companion object {
        fun <T> toPagedList(list: List<T>): PagedList<T> {
            val config = PagedList.Config.Builder()
                .setPageSize(list.size)
                .build()
            return PagedList.Builder(ListDataSource(list), config)
                .setFetchExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .setNotifyExecutor(object : Executor {
                    val handler = Handler(Looper.getMainLooper())
                    override fun execute(command: Runnable) {
                        handler.post(command)
                    }
                })
                .build()
        }
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<T>
    ) {
        val totalCount = mList.size
        val position = computeInitialLoadPosition(params, totalCount)
        val loadSize =
            computeInitialLoadSize(params, position, totalCount)

        // for simplicity, we could return everything immediately,
        // but we tile here since it's expected behavior
        val sublist = mList.subList(position, position + loadSize)
        callback.onResult(sublist, position, totalCount)
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<T>
    ) {
        callback.onResult(
            mList.subList(
                params.startPosition,
                params.startPosition + params.loadSize
            )
        )
    }
}