package com.wan.ui.classify

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.wan.data.classify.ClassifyFirstNode
import com.wan.data.classify.ClassifySecondNode

class ClassifyRecyclerAdapter : BaseNodeAdapter() {
    init {
        addNodeProvider(ClassifyFirstProvider())
        addNodeProvider(ClassifySecondProvider())
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when (data[position]) {
            is ClassifyFirstNode -> 1
            is ClassifySecondNode -> 2
            else -> -1
        }
    }

    companion object {
        const val EXPAND_COLLAPSE_PAYLOAD = 110
    }
}