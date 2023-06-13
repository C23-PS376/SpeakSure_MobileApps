package com.example.speaksure_capstone.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.speaksure_capstone.network.ApiService
import com.example.speaksure_capstone.response.CommentItem

class CommentPagingSource (private val apiService: ApiService, private val token: String, private val threadId: Int) : PagingSource<Int, CommentItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getCommentThread(token,threadId,page, params.loadSize)

            Log.e("API_Response", responseData.toString())

            LoadResult.Page(
                data = responseData.dataComment,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.dataComment.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CommentItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}