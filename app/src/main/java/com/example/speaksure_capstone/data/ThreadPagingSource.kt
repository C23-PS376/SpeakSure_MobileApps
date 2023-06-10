package com.example.speaksure_capstone.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.speaksure_capstone.network.ApiService
import com.example.speaksure_capstone.response.ListThreads

class ThreadPagingSource(private val apiService: ApiService, private val token: String, private val query: String) : PagingSource<Int, ListThreads>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListThreads> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = if(query == ""){
                Log.e("API_Response_home", ",mmasukk")
                apiService.getThread(token,page,params.loadSize)
            }else{
                apiService.searchThread(token,query,page, params.loadSize)
            }

            LoadResult.Page(
                data = responseData.data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.data.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListThreads>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}