package com.bean.simplenews.module.news.model;

import com.bean.simplenews.bean.NewsBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsListService {
    // http://c.m.163.com/nc/article/headline/T1348647909107/40-20.html
    @GET("{category}/{id}/{pageIndex}-20.html")
    Call<List<NewsBean>> loadNews(@Path("category") String category,
                                  @Path("id") String id,
                                  @Path("pageIndex") int pageIndex);
}
