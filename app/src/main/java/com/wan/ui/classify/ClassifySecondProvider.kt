package com.wan.ui.classify

import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wan.R
import com.wan.data.classify.ClassifySecondNode
import com.wan.ui.articles.ArticlesActivity
import com.wan.ui.articles.ClassifyArticlesFragment

class ClassifySecondProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.recycler_item_child_classify

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        (item as ClassifySecondNode).let {
            helper.setText(R.id.tv_title, it.model.name)
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        (data as ClassifySecondNode).let {
            ArticlesActivity.open(
                context, ArticlesActivity.TYPE_CLASSIFY,
                ClassifyArticlesFragment.args(it.model.classifyId, it.model.name)
            )
        }
    }
}