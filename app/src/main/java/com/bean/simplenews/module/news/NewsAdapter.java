package com.bean.simplenews.module.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.simplenews.R;
import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.util.ImageLoaderUtils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private List<NewsBean> mData;
    private int mType;
    private boolean mShowFooter=true;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public NewsAdapter(Context context,int type) {
        this.mContext = context;
        mType = type;
    }

    @Override
    public int getItemViewType(int position) {
        //F.e("fuck",""+position);
        if(position==0){
            return TYPE_HEADER;
        }else if (position+1==getItemCount()) {
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_header,parent,false);
            return new HeaderViewHolder(view);
        }else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            return new ItemViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            ImageLoaderUtils.display(mContext, ((HeaderViewHolder) holder).mImage, "http://binzhihao.github.io/header"+mType+".jpg");
        }
        if(holder instanceof ItemViewHolder) {
            NewsBean news = mData.get(position-1);
            if(news == null) {
                return;
            }
            ((ItemViewHolder) holder).mTitle.setText(news.getTitle());
            ((ItemViewHolder) holder).mDesc.setText(news.getDigest());
            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).mNewsImg, news.getImgsrc());
        }
        if(holder instanceof FooterViewHolder){
            if(mShowFooter){
                ((FooterViewHolder)holder).mTextView.setText(mContext.getString(R.string.loading));
            }else{
                ((FooterViewHolder)holder).mTextView.setText(" ");
            }
        }
    }

    @Override
    public int getItemCount() {
        int extra = 2;  //header and footer
        if(mData == null) {
            return extra;
        }
        return mData.size() + extra;
    }

    public void setDate(List<NewsBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public NewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void setShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public HeaderViewHolder(View view) {
            super(view);
            mImage=(ImageView) view.findViewById(R.id.img);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public FooterViewHolder(View view) {
            super(view);
            mTextView=(TextView)view.findViewById(R.id.more_data_msg);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mDesc;
        public ImageView mNewsImg;
        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tvTitle);
            mDesc = (TextView) v.findViewById(R.id.tvDesc);
            mNewsImg = (ImageView) v.findViewById(R.id.ivNews);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getPosition()-1);
            }
        }
    }

    /* 对外提供接口 */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
