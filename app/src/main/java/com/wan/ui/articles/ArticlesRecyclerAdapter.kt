package com.wan.ui.articles

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wan.R
import com.wan.data.articles.Article
import com.wan.view.BorderSpan

class ArticlesRecyclerAdapter :
    BaseQuickAdapter<Article, BaseViewHolder>(R.layout.recycler_item_article), LoadMoreModule {
    init {
        addChildClickViewIds(R.id.btn_collect, R.id.tv_classify)
    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        val hasClassify =
            !(item.chapterName.isNullOrEmpty() && item.superChapterName.isNullOrEmpty())
        val classify = StringBuilder()
        if (!item.superChapterName.isNullOrEmpty()) {
            classify.append(item.superChapterName)
        }
        if (!item.chapterName.isNullOrEmpty()) {
            if (classify.isNotEmpty()) {
                classify.append("/")
            }
            classify.append(item.chapterName)
        }
        val userName = when {
            !item.author.isNullOrEmpty() -> item.author
            !item.shareUser.isNullOrEmpty() -> item.shareUser
            else -> context.getString(R.string.unknown_user_date)
        }
        val date = when {
            !item.niceDate.isNullOrEmpty() -> item.niceDate
            !item.niceShareDate.isNullOrEmpty() -> item.niceShareDate
            else -> context.getString(R.string.unknown_user_date)
        }

        val title = SpannableStringBuilder()

        if (item.top == true) {
            title.append("置顶").append(" ")
            val borderSpan = createBorderSpan()
            title.setSpan(
                borderSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        if (item.fresh) {
            title.append("新").append(" ")
            val start = if (item.top == true) 3 else 0
            val borderSpan = createBorderSpan()
            title.setSpan(
                borderSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        title.append(item.title)

        holder.setText(R.id.tv_title, title)
            .setVisible(R.id.tv_classify, hasClassify)
            .setText(R.id.tv_classify, classify)

        holder.getView<ImageButton>(R.id.btn_collect).isSelected = item.collect

        val tvAuthorAndDate = holder.getView<TextView>(R.id.tv_author_date)
        val authorAndDate = SpannableStringBuilder("$userName $date")
        authorAndDate.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                // todo 点击进到作者页面 最好是在Fragment中回调
                if (context.getString(R.string.unknown_user_date) != userName) {
                    ArticlesActivity.open(
                        context,
                        ArticlesActivity.TYPE_AUTHOR,
                        AuthorArticlesFragment.args(userName)
                    )
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                if (context.getString(R.string.unknown_user_date) != userName) {
                    ds.color = context.resources.getColor(R.color.colorAccent)
                } else {
                    ds.color = context.resources.getColor(R.color.color_4D999999)
                }
            }
        }, 0, userName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAuthorAndDate.text = authorAndDate
        tvAuthorAndDate.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun convert(holder: BaseViewHolder, item: Article, payloads: List<Any>) {
        for (payload in payloads) {
            if (payload is Int && payload == ArticlesDiffCallback.PAYLOAD_COLLECT) {
                holder.getView<ImageButton>(R.id.btn_collect).isSelected = item.collect
            }
        }
    }

    private fun createBorderSpan(): BorderSpan {
        return BorderSpan(
            context.resources.getColor(R.color.colorSecondary),
            context.resources.getDimension(R.dimen.dp_4),
            context.resources.getDimension(R.dimen.dp_1),
            context.resources.getColor(R.color.colorSecondary),
            context.resources.getDimension(R.dimen.sp_12),
            context.resources.getDimension(R.dimen.dp_4),
            context.resources.getDimension(R.dimen.dp_1)
        )
    }
}