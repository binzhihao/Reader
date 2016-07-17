package com.bean.simplenews.module.news.model.converter;

import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.util.LogUtils;
import com.google.gson.JsonParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class NewsListConverterFactory extends Converter.Factory{

    private JsonParser parser;

    public static NewsListConverterFactory create() {
        LogUtils.e("fuck","factory created");
        return new NewsListConverterFactory();
    }

    private NewsListConverterFactory() {
        parser=new JsonParser();
    }

    /*这个方法每一次http请求都被调用，返回一个新建的Converter对象，因此每一个http线程都持有一个Converter对象，
     *彼此不会发生冲突。
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new NewsListResponseBodyConverter<List<NewsBean>>(parser);
    }
}