package com.bean.simplenews.module.news.model.converter;

import com.bean.simplenews.module.news.model.bean.NewsDetailBean;
import com.google.gson.JsonParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class NewsDetailConverterFactory extends Converter.Factory{

    private JsonParser parser;

    public static NewsDetailConverterFactory create() {
        return new NewsDetailConverterFactory();
    }

    private NewsDetailConverterFactory() {
        parser = new JsonParser();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new NewsDetailResponseBodyConverter<NewsDetailBean>(parser);
    }
}
