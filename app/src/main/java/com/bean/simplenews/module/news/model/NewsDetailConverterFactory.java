package com.bean.simplenews.module.news.model;

import com.bean.simplenews.bean.NewsDetailBean;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class NewsDetailConverterFactory extends Converter.Factory{

    public static NewsDetailConverterFactory create() {
        return new NewsDetailConverterFactory();
    }

    private NewsDetailConverterFactory() {}

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new NewsDetailResponseBodyConverter<NewsDetailBean>();
    }
}
