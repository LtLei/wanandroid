package com.wan.ui.classify

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import androidx.core.view.ViewCompat
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wan.R
import com.wan.data.classify.ClassifyFirstNode


class ClassifyFirstProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.recycler_item_parent_classify

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        (item as ClassifyFirstNode).let {
            helper.setText(R.id.tv_title, it.model.name)
            setArrowSpin(helper, item, false)
        }
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        for (payload in payloads) {
            if (payload is Int && payload == ClassifyRecyclerAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, item, true)
            }
        }
    }

    private fun setArrowSpin(
        helper: BaseViewHolder,
        data: BaseNode,
        isAnimate: Boolean
    ) {
        val entity: ClassifyFirstNode = data as ClassifyFirstNode
        val imageButton: ImageButton = helper.getView(R.id.iv_arrow)
        if (entity.isExpanded) {
            if (isAnimate) {
                ViewCompat.animate(imageButton).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(180f)
                    .start()
            } else {
                imageButton.rotation = 180f
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageButton).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(0f)
                    .start()
            } else {
                imageButton.rotation = 0f
            }
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter()?.expandOrCollapse(
            position,
            animate = true,
            notify = true,
            parentPayload = ClassifyRecyclerAdapter.EXPAND_COLLAPSE_PAYLOAD
        );
    }
}