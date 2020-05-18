package com.wan.ui.articles

import androidx.recyclerview.widget.DiffUtil
import com.wan.data.articles.Article

class ArticlesDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id && oldItem.collect == newItem.collect
    }

    // 局部刷新
    override fun getChangePayload(oldItem: Article, newItem: Article): Any? {
        if (oldItem.collect != newItem.collect) {
            return PAYLOAD_COLLECT
        }
        return null
    }

    companion object {
        const val PAYLOAD_COLLECT = 1
    }
}