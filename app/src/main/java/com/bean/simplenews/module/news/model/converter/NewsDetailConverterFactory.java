package com.bean.simplenews.module.news.model.converter;

import com.bean.simplenews.module.news.model.bean.NewsDetailBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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
