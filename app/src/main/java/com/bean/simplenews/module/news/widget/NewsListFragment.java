package com.bean.simplenews.module.news.widget;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bean.simplenews.R;
import com.bean.simplenews.api.Urls;
import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.common.base.BaseFragment;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.module.news.NewsAdapter;
import com.bean.simplenews.module.news.presenter.NewsListPresenter;
import com.bean.simplenews.module.news.view.NewsListView;
import com.bean.simplenews.util.CacheUtils;
import com.bean.simplenews.util.LogUtils;
import com.bean.simplenews.util.ToastUtils;

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
    private List<NewsBean> mData, mCache;
    private int mPageIndex = -Urls.PAGE_SIZE;
    private boolean mFirstTime = true;

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
        initPresenter(new NewsListPresenter(this, getArguments().getInt(Constants.TYPE), getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newslist, null);
        unbinder = ButterKnife.bind(this, view);

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
                String docId = mAdapter.getItem(position).getDocid();
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra(Constants.NEWS, docId);
                // android.support.v4.app.ActivityCompat is a helper for accessing features in Activity introduced after API level 4
                // in a backwards compatible fashion. Here it start a activity with a Bundle.
                ActivityCompat.startActivity(getActivity(), intent, null);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisibleItem, lastVisibleItem;
            private boolean isUp;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isUp = dy < 0;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItem + 1 >= mAdapter.getItemCount() && !isUp) {
                        //向下加载更多
                        showFooterProgress();
                        obtainPresenter().loadNews(mPageIndex + Urls.PAGE_SIZE, false);
                    } else if (firstVisibleItem <= 2 && isUp && mPageIndex > Urls.PAGE_SIZE) {
                        //向上加载更多
                        obtainPresenter().loadNews(mPageIndex - Urls.PAGE_SIZE * 2, true);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirstTime) {
            mFirstTime = false;
            //主线程运行，延迟加载，提高切换性能
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    obtainPresenter().loadNews(0,false);
                }
            }, 1000);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public synchronized void setNewsData(List<NewsBean> newsList, boolean isUp) {
        if (newsList == null || newsList.size() == 0) {
            ToastUtils.makeToast(this.getContext(), getString(R.string.load_no_data));
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (!isUp) {
            if (mPageIndex == -Urls.PAGE_SIZE || mPageIndex == 0){
                mData.addAll(newsList);
                mAdapter.setDate(mData, mPageIndex + Urls.PAGE_SIZE);
                mAdapter.notifyDataSetChanged();
            } else {
                int count = mData.size() - mCache.size();
                mData.clear();
                mData.addAll(mCache);
                mData.addAll(newsList);
                mAdapter.setDate(mData, mPageIndex);
                mAdapter.notifyItemRangeRemoved(0, count);
                mAdapter.notifyItemRangeInserted(mCache.size(), newsList.size());
            }
            mCache = newsList;
            mPageIndex += Urls.PAGE_SIZE;
        } else {
            int count1 = mData.size() - mCache.size();
            int count2 = mCache.size();
            mCache.clear();
            mCache.addAll(mData.subList(0, count1));
            mData.clear();
            mData.addAll(newsList);
            mData.addAll(mCache);
            mAdapter.setDate(mData, mPageIndex - Urls.PAGE_SIZE);
            mAdapter.notifyItemRangeRemoved(count1, count2);
            mAdapter.notifyItemRangeInserted(0, newsList.size());
            mPageIndex -= Urls.PAGE_SIZE;
        }
    }

    @Override
    public void showLoadFailure() {
        hideFooterProgress();
        mSwipeRefreshWidget.setRefreshing(false);
        ToastUtils.makeToast(getActivity(), getString(R.string.load_fail));
    }

    @Override
    public void showLoadSuccess() {
        hideFooterProgress();
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (mData != null) {
            mData.clear();
        }
        obtainPresenter().clearCache();  //清空缓存
        mPageIndex = -Urls.PAGE_SIZE;
        obtainPresenter().loadNews(0, false);
        mSwipeRefreshWidget.setRefreshing(true);
    }

    private void showFooterProgress() {
        if (!mAdapter.isShowFooter()) {
            mAdapter.setShowFooter(true);
            mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
        }
    }


    private void hideFooterProgress() {
        if (mAdapter.isShowFooter()) {
            mAdapter.setShowFooter(false);
            mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
        }
    }

}