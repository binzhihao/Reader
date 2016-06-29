package com.bean.simplenews.module.news.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bean.simplenews.R;
import com.bean.simplenews.api.Urls;
import com.bean.simplenews.bean.NewsBean;
import com.bean.simplenews.common.base.BaseFragment;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.module.news.NewsAdapter;
import com.bean.simplenews.module.news.presenter.NewsListPresenter;
import com.bean.simplenews.module.news.view.NewsListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsListFragment extends BaseFragment<NewsListPresenter> implements NewsListView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshWidget;
    private Unbinder unbinder;
    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<NewsBean> mData;
    private int mType = 0, mPageIndex = 0;

    public static NewsListFragment newInstance(int type) {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        args.putInt(Constants.TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(Constants.TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newslist, null);
        initPresenter(new NewsListPresenter(this));
        unbinder=ButterKnife.bind(this, view);

        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary_dark, R.color.colorAccent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NewsAdapter(getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsBean news = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra(Constants.NEWS, news);
                // android.support.v4.app.ActivityCompat is a helper for accessing features in Activity introduced after API level 4
                // in a backwards compatible fashion. Here it start a activity with a Bundle.
                ActivityCompat.startActivity(getActivity(), intent, null);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount() && mAdapter.isShowFooter()) {
                    /* 加载更多 */
                    obtainPresenter().loadNews(mType, mPageIndex + Urls.PAZE_SIZE);
                }
            }
        });
        onRefresh();    // 立即刷新一次
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void addNews(List<NewsBean> newsList) {
        mAdapter.isShowFooter(true);
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(newsList);
        if (mPageIndex == 0) {
            mAdapter.setDate(mData);
        } else {
            //如果没有更多数据了,则隐藏footer布局
            if (newsList == null || newsList.size() == 0) {
                mAdapter.isShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        mPageIndex += Urls.PAZE_SIZE;
    }

    @Override
    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailure() {
        if (mPageIndex == 0) {
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
        //mSwipeRefreshWidget.setBackgroundResource(R.drawable.base_empty_view);
    }

    @Override
    public void showLoadSuccess() {
        //mSwipeRefreshWidget.setBackgroundColor(getResources().getColor(R.color.divider));
    }

    @Override
    /* implement the interface from SwipeRefreshLayout */
    public void onRefresh() {
        mPageIndex = 0;
        if (mData != null) {
            mData.clear();
        }
        obtainPresenter().loadNews(mType, mPageIndex);
    }

}