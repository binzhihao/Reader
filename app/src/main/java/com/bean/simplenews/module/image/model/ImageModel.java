package com.bean.simplenews.module.image.model;

/**
 * Description :
 * Author : bean
 * Email  : bean.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 15/12/22
 */
public interface ImageModel {
    void loadImageList(ImageModelImpl.OnLoadImageListListener listener);
}
