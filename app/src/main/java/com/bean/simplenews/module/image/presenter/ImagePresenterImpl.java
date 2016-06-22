package com.bean.simplenews.module.image.presenter;

import com.bean.simplenews.bean.ImageBean;
import com.bean.simplenews.module.image.model.ImageModel;
import com.bean.simplenews.module.image.model.ImageModelImpl;
import com.bean.simplenews.module.image.view.ImageView;

import java.util.List;

public class ImagePresenterImpl implements ImagePresenter, ImageModelImpl.OnLoadImageListListener {

    private ImageModel mImageModel;
    private ImageView mImageView;

    public ImagePresenterImpl(ImageView imageView) {
        this.mImageModel = new ImageModelImpl();
        this.mImageView = imageView;
    }

    @Override
    public void loadImageList() {
        mImageView.showProgress();
        mImageModel.loadImageList(this);
    }

    @Override
    public void onSuccess(List<ImageBean> list) {
        mImageView.addImages(list);
        mImageView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mImageView.hideProgress();
        mImageView.showLoadFailMsg();
    }
}
