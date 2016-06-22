package com.bean.simplenews.module.image.model;

import com.bean.simplenews.bean.ImageBean;
import com.bean.simplenews.api.Urls;
import com.bean.simplenews.module.image.ImageJsonUtils;
import com.bean.simplenews.util.OkHttpUtils;

import java.util.List;

/**
 * Description :
 * Author : bean
 * Email  : bean.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 15/12/22
 */
public class ImageModelImpl implements ImageModel {

    /**
     * 获取图片列表
     * @param listener
     */
    @Override
    public void loadImageList(final OnLoadImageListListener listener) {
        String url = Urls.IMAGES_URL;
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<ImageBean> iamgeBeanList = ImageJsonUtils.readJsonImageBeans(response);
                listener.onSuccess(iamgeBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load image list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    public interface OnLoadImageListListener {
        void onSuccess(List<ImageBean> list);
        void onFailure(String msg, Exception e);
    }
}
