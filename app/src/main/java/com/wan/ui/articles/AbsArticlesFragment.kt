package com.wan.ui.articles

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wan.R
import com.wan.WebViewActivity
import com.wan.bus.LogInOutBus
import com.wan.core.Resource
import com.wan.core.State
import com.wan.core.base.BaseFragment
import com.wan.core.event.EventObserver
import com.wan.core.extensions.autoCleared
import com.wan.data.articles.ArticlesResult
import com.wan.data.user.UserManager
import com.wan.ui.login.LoginActivity
import kotlinx.android.synthetic.main.abs_articles_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

/**
 * 本项目里大多数页面都是文章列表，可以简单抽取一下。
 */
abstract class AbsArticlesFragment<T : AbsArticlesViewModel> :
    BaseFragment(R.layout.abs_articles_fragment) {

    private var initialized = false

    abstract val viewModel: T

    private var articlesRecyclerAdapter by autoCleared<ArticlesRecyclerAdapter>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLogInOutEvent(logInOutBus: LogInOutBus) {
        // 登录状态发生改变，刷新当前页面
        Timber.d("loginStateChange ----> isLogin : ${logInOutBus.login}")
        viewModel.refresh(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        articlesRecyclerAdapter = ArticlesRecyclerAdapter().apply {
            setDiffCallback(ArticlesDiffCallback())

            loadMoreModule.setOnLoadMoreListener { viewModel.loadMore() }
            setOnItemClickListener { _, _, position ->
                WebViewActivity.open(
                    requireContext(),
                    getItem(position).link,
                    getItem(position).title
                )
            }
            setOnItemChildClickListener { _, view, position ->
                val item = getItem(position)
                if (view.id == R.id.btn_collect) {
                    if (!UserManager.getInstance().isLogin()) {
                        startActivity(Intent(context, LoginActivity::class.java))
                        return@setOnItemChildClickListener
                    }

                    // 是否去收藏
                    val collectIntent = !item.collect
                    if (collectIntent) {
                        viewModel.collectArticle(item.id, position)
                    } else {
                        viewModel.unCollectArticle(item.id, position)
                    }
                } else {
                    // 分类
                    if (item.chapterId != 0 && !item.chapterName.isNullOrEmpty()) {
                        ArticlesActivity.open(
                            requireContext(),
                            ArticlesActivity.TYPE_CLASSIFY,
                            ClassifyArticlesFragment.args(
                                item.chapterId,
                                item.chapterName
                            )
                        )
                    }
                }
            }
        }

        recycler.adapter = articlesRecyclerAdapter

        swipe_refresh.setOnRefreshListener {
            viewModel.refresh(true)
            articlesRecyclerAdapter.loadMoreModule.isEnableLoadMore = false
        }

        swipe_refresh.isEnabled = false
        swipe_refresh.setColorSchemeResources(
            R.color.colorAccent,
            android.R.color.holo_orange_light,
            android.R.color.holo_green_light,
            android.R.color.holo_blue_light
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // observes.
        lifecycleScope.launchWhenResumed {
            viewModel.articles.observe(viewLifecycleOwner, Observer { resource ->
                if (swipe_refresh.isEnabled) {
                    swipe_refresh.isRefreshing = resource.state == State.LOADING
                }

                if (resource.state == State.LOADING) {
                    if (!hasShownContent()) {
                        showLoading()
                    }
                    articlesRecyclerAdapter.loadMoreModule.isEnableLoadMore = false
                    return@Observer
                }

                val result =
                    requireNotNull(resource.data, { "we are sure that data is not null." })
                if (!result.articles.isNullOrEmpty()) {
                    swipe_refresh.isEnabled = true
                    // 等待adapter数据加载完毕，再更新状态
                    articlesRecyclerAdapter.getDiffHelper()
                        .submitList(result.articles, Runnable { updateState(resource) })
                } else {
                    // 可以直接更新状态
                    updateState(resource)
                }
            })

            viewModel.collectStatus.observe(viewLifecycleOwner, EventObserver {
                when (it.state) {
                    State.SUCCESS -> {
                        it.data?.let { collectStatus ->
                            articlesRecyclerAdapter.apply {
                                // collect
                                data[collectStatus.position].collect =
                                    data[collectStatus.position].collect.not()
                                notifyItemChanged(
                                    collectStatus.position,
                                    ArticlesDiffCallback.PAYLOAD_COLLECT
                                )
                            }
                            if (collectStatus.isCollect) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.collect_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.un_collect_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    State.ERROR -> {
                        // it.data有值
                        it.data?.let { collectStatus ->
                            if (collectStatus.isCollect) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.collect_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.un_collect_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    else -> {
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (!initialized) {
            initialized = true
            viewModel.refresh(false)
        }
    }

    private fun updateState(resource: Resource<ArticlesResult>) {
        val result = requireNotNull(resource.data)
        when (resource.state) {
            State.SUCCESS -> {
                if (!hasShownContent()) {
                    showContent()
                }

                if (result.refresh) {
                    articlesRecyclerAdapter.loadMoreModule.isEnableLoadMore = true

                    if (!result.hasMore) {
                        articlesRecyclerAdapter.loadMoreModule.loadMoreEnd()
                    }
                } else {
                    if (result.hasMore) {
                        articlesRecyclerAdapter.loadMoreModule.loadMoreComplete()
                    } else {
                        articlesRecyclerAdapter.loadMoreModule.loadMoreEnd()
                    }
                }
            }
            State.EMPTY -> {
                if (result.refresh) {
                    showEmpty()
                } else {
                    articlesRecyclerAdapter.loadMoreModule.loadMoreEnd()
                }
            }
            State.ERROR -> {
                if (result.refresh) {
                    showError()
                } else {
                    articlesRecyclerAdapter.loadMoreModule.loadMoreFail()
                }
            }
            else -> require(false) // unreachable code.
        }
    }

    private fun initRecycler() {
        recycler.layoutManager = LinearLayoutManager(requireContext())
        if (recycler.itemDecorationCount == 0) {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_articles
            )?.let { drawable ->
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                    .also { divider -> divider.setDrawable(drawable) }
            }?.let { divider -> recycler.addItemDecoration(divider) }
        }
    }

    override fun retry() {
        showLoading()
        viewModel.refresh(false)
    }

    fun runToTop() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            recycler.scrollToPosition(0)
        }
    }
}