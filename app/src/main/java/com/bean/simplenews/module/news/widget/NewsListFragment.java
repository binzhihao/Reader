package com.bean.simplenews.module.news.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bean.simplenews.R;
import com.bean.simplenews.bean.NewsBean;
import com.bean.simplenews.api.Urls;
import com.bean.simplenews.module.news.NewsAdapter;
import com.bean.simplenews.module.news.NewsConstants;
import com.bean.simplenews.module.news.presenter.NewsPresenter;
import com.bean.simplenews.module.news.presenter.NewsPresenterImpl;
import com.bean.simplenews.module.news.view.NewsView;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment implements NewsView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "NewsListFragment";
    private static final String TYPE = "Type";


    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<NewsBean> mData;
    private NewsPresenter mNewsPresenter;
    private int mType = NewsConstants.NEWS_TYPE_TOP;
    private int pageIndex = 0;

    public static NewsListFragment newInstance(int type) {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        args.putInt(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsPresenter = new NewsPresenterImpl(this);
        mType = getArguments().getInt(TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, null);

        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.primary_light, R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
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
                intent.putExtra(NewsConstants.NEWS, news);
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
                    mNewsPresenter.loadNews(mType, pageIndex + Urls.PAZE_SIZE);
                }
            }
        });

        onRefresh();    // 立即刷新一次
        return view;
    }

    @Override
    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void addNews(List<NewsBean> newsList) {
        mAdapter.isShowFooter(true);
        if(mData == null) {
            mData = new ArrayList<NewsBean>();
        }
        mData.addAll(newsList);
        if(pageIndex == 0) {
            mAdapter.setDate(mData);
        } else {
            //如果没有更多数据了,则隐藏footer布局
            if(newsList == null || newsList.size() == 0) {
                mAdapter.isShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        pageIndex += Urls.PAZE_SIZE;
    }


    @Override
    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        if(pageIndex == 0) {
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
        mSwipeRefreshWidget.setBackgroundResource(R.drawable.base_empty_view);
    }

    @Override
    public void LoadSuccess(){
        mSwipeRefreshWidget.setBackgroundColor(getResources().getColor(R.color.divider));
    }

    @Override
    /* implement the interface from SwipeRefreshLayout */
    public void onRefresh() {
        pageIndex = 0;
        if(mData != null) {
            mData.clear();
        }
        mNewsPresenter.loadNews(mType, pageIndex);
    }

}