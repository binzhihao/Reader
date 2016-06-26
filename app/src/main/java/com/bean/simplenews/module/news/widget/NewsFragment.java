package com.bean.simplenews.module.news.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bean.simplenews.R;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.common.adapter.BaseFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        unbinder=ButterKnife.bind(this, view);

        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(NewsListFragment.newInstance(Constants.NEWS_TYPE_TOP), getString(R.string.top));
        adapter.addFragment(NewsListFragment.newInstance(Constants.NEWS_TYPE_NBA), getString(R.string.nba));
        adapter.addFragment(NewsListFragment.newInstance(Constants.NEWS_TYPE_CARS), getString(R.string.cars));
        adapter.addFragment(NewsListFragment.newInstance(Constants.NEWS_TYPE_JOKES), getString(R.string.jokes));
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager);  // 将两者关联起来

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
          unbinder.unbind();  // necessary in fragment
    }
}
