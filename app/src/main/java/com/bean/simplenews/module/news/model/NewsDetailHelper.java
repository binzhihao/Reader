package com.bean.simplenews.module.news.model;

import com.bean.simplenews.bean.NewsDetailBean;
import com.bean.simplenews.module.news.model.converter.NewsDetailConverterFactory;

import java.util.Hashtable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsDetailHelper {

    private Retrofit retrofit;
    private NewsDetailService service;
    private Hashtable<String,Call<NewsDetailBean>> callMap; //Hashtable是线程安全的
    private static NewsDetailHelper instance;

    private NewsDetailHelper(){
        retrofit=new Retrofit.Builder()
                .baseUrl("http://c.m.163.com/nc/article/")
                .addConverterFactory(NewsDetailConverterFactory.create())
                .build();
        service=retrofit.create(NewsDetailService.class);
        callMap=new Hashtable<>();
    }

    public static NewsDetailHelper getInstance() {
        if(instance==null){
            instance = new NewsDetailHelper();
        }
        return instance;
    }

    public void loadNewsDetail(final String id, final OnLoadNewsDetailListener listener){
        Call<NewsDetailBean> NewsDetailCall=service.loadNewsDetail(id);
        callMap.put(id,NewsDetailCall);
        //异步回调
        NewsDetailCall.enqueue(new Callback<NewsDetailBean>() {
            @Override
            public void onResponse(Call<NewsDetailBean> call, Response<NewsDetailBean> response) {
                callMap.remove(id);
                if(listener!=null) listener.onSuccess(response.body());
            }
            @Override
            public void onFailure(Call<NewsDetailBean> call, Throwable t) {
                //LogUtils.e("fuck","fail "+t.getMessage());
                callMap.remove(id);
                if(listener!=null) listener.onFailure(t);
            }
        });
    }

    public void cancel(String key){
        if(isCallExist(key)){
            callMap.get(key).cancel();
            callMap.remove(key);
        }
    }

    public void cancelAll(String type){
        //TODO
    }

    public boolean isCallExist(String key){
        return (!callMap.isEmpty() && callMap.containsKey(key));
    }

    public interface OnLoadNewsDetailListener {
        void onSuccess(NewsDetailBean bean);
        void onFailure(Throwable t);
    }
}
