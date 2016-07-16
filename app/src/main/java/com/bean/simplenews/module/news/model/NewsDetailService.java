package com.bean.simplenews.module.news.model;

import com.bean.simplenews.module.news.model.bean.NewsDetailBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsDetailService {
    //http://c.m.163.com/nc/article/{docId}/full.html
    @GET("{docId}/full.html")
    Call<NewsDetailBean> loadNewsDetail(@Path("docId") String id);
}
