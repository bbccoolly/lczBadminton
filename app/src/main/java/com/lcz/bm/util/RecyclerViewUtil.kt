package com.lcz.bm.util

import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

/**
 * desc: TODO
 *
 *
 * create by Arrow on 2020-11-10
 */
class RecyclerViewUtil @Inject constructor(){
    fun scrollTo(recyclerView: RecyclerView) {
        recyclerView.layoutManager?.smoothScrollToPosition(
            recyclerView,
            null,
            recyclerView.adapter!!.itemCount - 1
        )
    }
}