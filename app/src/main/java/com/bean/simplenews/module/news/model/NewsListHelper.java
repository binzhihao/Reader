package com.bean.simplenews.module.news.model;

import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.module.news.model.converter.NewsListConverterFactory;
import com.bean.simplenews.util.LogUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsListHelper {

    private Retrofit retrofit;
    private NewsListService service;
    private Hashtable<String, Call<List<NewsBean>>> callMap; //Hashtable是线程安全的
    private static NewsListHelper instance;

    private NewsListHelper() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://c.m.163.com/nc/article/")
                .addConverterFactory(NewsListConverterFactory.create())
                .build();
        service = retrofit.create(NewsListService.class);
        callMap = new Hashtable<>();
    }

    public static NewsListHelper getInstance() {
        if (instance == null) {
            instance = new NewsListHelper();
        }
        return instance;
    }

    public void loadNews(final String category, final String id, final int pageIndex,
                         final OnLoadNewsListListener listener, final boolean isUp) {
        Call<List<NewsBean>> newsListCall = service.loadNews(category, id, pageIndex);
        callMap.put(id, newsListCall);
        //异步回调
        newsListCall.enqueue(new Callback<List<NewsBean>>() {
            @Override
            public void onResponse(Call<List<NewsBean>> call, Response<List<NewsBean>> response) {
                callMap.remove(id);
                if (listener != null)
                    listener.onSuccess(response.body(), pageIndex, isUp);
            }

            @Override
            public void onFailure(Call<List<NewsBean>> call, Throwable t) {
                callMap.remove(id);
                if (listener != null) listener.onFailure(t);
            }
        });
    }

    //cancel previous request
    public void cancel(String key) {
        if (isCallExist(key)) {
            callMap.get(key).cancel();
            callMap.remove(key);
        }
    }

    private boolean isCallExist(String key) {
        return (!callMap.isEmpty() && callMap.containsKey(key));
    }

    public interface OnLoadNewsListListener {
        void onSuccess(List<NewsBean> list, int pageIndex, boolean isUp);

        void onFailure(Throwable t);
    }
}
