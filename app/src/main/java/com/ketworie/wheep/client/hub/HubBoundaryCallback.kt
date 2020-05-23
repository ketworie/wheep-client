package com.ketworie.wheep.client.hub

import androidx.paging.PagedList
import com.ketworie.wheep.client.room.PagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class HubBoundaryCallback(
    val hubService: HubService,
    val handleResponse: (List<Hub>) -> Unit,
    val ioExecutor: Executor
) : PagedList.BoundaryCallback<Hub>() {


    val helper = PagingRequestHelper(ioExecutor)

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            hubService.getMyHubs().enqueue(object : Callback<List<Hub>> {
                override fun onFailure(call: Call<List<Hub>>, t: Throwable) {
                    it.recordFailure(t)
                }

                override fun onResponse(call: Call<List<Hub>>, response: Response<List<Hub>>) {
                    ioExecutor.execute {
                        response.body()?.let(handleResponse)
                        it.recordSuccess()
                    }
                }
            })
        }
    }

}