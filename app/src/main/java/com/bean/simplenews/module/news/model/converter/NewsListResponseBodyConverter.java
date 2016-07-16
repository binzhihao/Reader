package com.bean.simplenews.module.news.model.converter;

import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.util.JsonUtils;
import com.bean.simplenews.util.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class NewsListResponseBodyConverter<T> implements Converter<ResponseBody,T> {

    //有时候会解析没有完成就返回，需要改进，尽量提高解释效率
    @Override public T convert(ResponseBody body) throws IOException {

        List<NewsBean> beans = new ArrayList<>();

        String value = body.string();
        //LogUtils.e("fuck",value);
        int indexA = value.indexOf('"'); int indexB = value.indexOf('"',5);
        String typeId = value.substring(indexA+1,indexB);
        LogUtils.e("fuck",typeId);

        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(value).getAsJsonObject();
        JsonElement jsonElement = jsonObj.get(typeId);
        if(jsonElement == null) {
            LogUtils.e("fuck","jsonElement is null");
            return null;
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        //LogUtils.e("fuck",""+jsonArray.size());
        for (int i = 1; i < jsonArray.size(); i++) {
            JsonObject jo = jsonArray.get(i).getAsJsonObject();
            if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                continue;
            }
            if (jo.has("TAGS") && !jo.has("TAG")) {
                continue;
            }

            if (!jo.has("imgextra")) {
                NewsBean news = JsonUtils.deserialize(jo, NewsBean.class);
                beans.add(news);
            }
        }
        return (T)beans;
    }
}
