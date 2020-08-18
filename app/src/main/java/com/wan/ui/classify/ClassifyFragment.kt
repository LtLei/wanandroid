package com.wan.ui.classify

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wan.R
import com.wan.core.State
import com.wan.core.base.BaseFragment
import com.wan.core.extensions.autoCleared
import com.wan.data.classify.ClassifyFirstNode
import kotlinx.android.synthetic.main.classify_fragment.*
import kotlinx.coroutines.launch

class ClassifyFragment : BaseFragment(R.layout.classify_fragment, false) {
    private val factory by lazy { ClassifyViewModelFactory() }
    private val viewModel by viewModels<ClassifyViewModel> {
        factory
    }

    private var hasInitialized = false

    private var mClassifyRecyclerAdapter by autoCleared<ClassifyRecyclerAdapter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contentView = layoutInflater.inflate(R.layout.classify_fragment_content, null)
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        if (recyclerView.itemDecorationCount == 0) {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_classifies
            )?.let { drawable ->
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                ).also { decoration ->
                    decoration.setDrawable(drawable)
                }
            }?.let { decoration ->
                recyclerView.addItemDecoration(decoration)
            }
        }

        mClassifyRecyclerAdapter = ClassifyRecyclerAdapter()
        recyclerView.adapter = mClassifyRecyclerAdapter

        loading.setContentView(contentView)
        loading.setRetryAction {
            lifecycleScope.launch {
                viewModel.retry()
            }
        }
        mLoadingLayout = loading
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            viewModel.classifies.observe(viewLifecycleOwner, { res ->
                when (res.state) {
                    State.LOADING -> {
                        showLoading()
                    }
                    State.ERROR -> {
                        showError()
                    }
                    State.EMPTY -> {
                        showEmpty()
                    }
                    State.SUCCESS -> {
                        showContent()
                        res.data?.let { models ->
                            models.mapIndexed { index, model ->
                                ClassifyFirstNode(model).also {
                                    it.isExpanded = index == 0
                                }
                            }
                        }?.let {
                            mClassifyRecyclerAdapter.setList(it)
                        }
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasInitialized) {
            viewModel.getClassifies()
            hasInitialized = true
        }
    }
}