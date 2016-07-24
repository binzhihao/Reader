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
    private List<NewsBean> mData;
    private int mType = 0, mPageIndex = 0;
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
        mType = getArguments().getInt(Constants.TYPE);
        initPresenter(new NewsListPresenter(this,mType));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newslist, null);
        unbinder=ButterKnife.bind(this, view);

        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary_dark, R.color.colorAccent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NewsAdapter(getActivity().getApplicationContext(),mType);
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
            private int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1>=mAdapter.getItemCount()) {
                    /* 加载更多 */
                    obtainPresenter().loadNews(mPageIndex + Urls.PAGE_SIZE);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mFirstTime){
            LogUtils.e("fuck","load in start");
            mFirstTime=false;
            //主线程运行，延迟加载，提高切换性能
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    obtainPresenter().loadNews(mPageIndex);
                }
            },1000);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void addNews(List<NewsBean> newsList) {
        //没有更多数据了
        if(newsList == null || newsList.size() == 0) {
            LogUtils.e("fuck","return news list is null or size is 0");
            return;
        }
        if(mData==null){
            mData=new ArrayList<>();
        }
        mData.addAll(newsList);
        mAdapter.setDate(mData);
        mPageIndex += Urls.PAGE_SIZE;
    }

    @Override
    public void showFooterProgress(){
        if(!mAdapter.isShowFooter()) {
            mAdapter.setShowFooter(true);
            mAdapter.notifyDataSetChanged();  //使footer的更改生效
        }
    }

    @Override
    public void hideFooterProgress(){
        if(mAdapter.isShowFooter()) {
            mAdapter.setShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoadFailure() {
        LogUtils.e("fuck","failure");
        hideFooterProgress();
        mSwipeRefreshWidget.setRefreshing(false);
        ToastUtils.makeToast(getActivity(),getString(R.string.load_fail));
    }

    @Override
    public void showLoadSuccess() {
        LogUtils.e("fuck","success");
        hideFooterProgress();
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    /* implement the interface from SwipeRefreshLayout */
    public void onRefresh() {
        if (mData != null) {
            mData.clear();
        }
        mPageIndex=0;
        obtainPresenter().loadNews(mPageIndex);
    }

}