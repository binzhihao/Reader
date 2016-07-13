package com.bean.simplenews.module.news.model.converter;

import com.bean.simplenews.bean.NewsDetailBean;
import com.bean.simplenews.util.JsonUtils;
import com.bean.simplenews.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class NewsDetailResponseBodyConverter<T> implements Converter<ResponseBody,T> {

    @Override
    public T convert(ResponseBody body) throws IOException {
        NewsDetailBean newsDetailBean;
        String value = body.string();
        int indexA = value.indexOf('"'); int indexB = value.indexOf('"',5);
        String docId = value.substring(indexA+1,indexB);
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(value).getAsJsonObject();
        JsonElement jsonElement = jsonObj.get(docId);
        if(jsonElement == null) {
            return null;
        }
        newsDetailBean = JsonUtils.deserialize(jsonElement.getAsJsonObject(), NewsDetailBean.class);
        return (T)newsDetailBean;
    }
}
