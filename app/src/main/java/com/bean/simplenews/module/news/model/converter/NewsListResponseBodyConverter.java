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

    private JsonParser parser;

    NewsListResponseBodyConverter(JsonParser jsonParser){
        parser=jsonParser;
    }

    @Override public T convert(ResponseBody body) throws IOException {

        List<NewsBean> beans = new ArrayList<>();

        String value = body.string();
        int indexA = value.indexOf('"'); int indexB = value.indexOf('"',5);
        String typeId = value.substring(indexA+1,indexB);

        JsonObject jsonObj = parser.parse(value).getAsJsonObject();
        JsonElement jsonElement = jsonObj.get(typeId);
        if(jsonElement == null) {
            LogUtils.e("fuck","jsonElement is null");
            return null;
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        int arraySize = jsonArray.size();
        if (arraySize > 1){
            for (int i =0; i < arraySize; i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {  // 除去专题
                    continue;
                }
                if (jo.has("imgextra") && !jo.has("hasCover")){  // 除去图集，但保留页头
                    continue;
                }
                NewsBean news = JsonUtils.deserialize(jo, NewsBean.class);
                beans.add(news);
            }
        }
        return (T)beans;
    }
}
