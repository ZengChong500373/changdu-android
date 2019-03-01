package org.linyi.viva.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.api.changdu.proto.BookApi;
import com.bumptech.glide.Glide;

import org.linyi.base.ui.CornerTransform;
import org.linyi.base.utils.UIUtils;

import org.linyi.viva.ui.activity.BookDetailActivity;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    List<BookApi.BookOverView> list;

    SparseArray<ImageView> imgList = new SparseArray<>();

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(imgList.valueAt(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        ImageView imageView = imgFactory(position, container.getContext());
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(container.getContext(), BookDetailActivity.class);
                intent.putExtra("id", list.get(position).getBookID());
                container.getContext().startActivity(intent);
            }
        });
        return imageView;
    }

    public void setData(List<BookApi.BookOverView> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ImageView imgFactory(int position, Context mContext) {
        ImageView img = imgList.valueAt(position);
        if (img == null) {
            ImageView imageView = new ImageView(mContext);
            CornerTransform transformation = new CornerTransform(mContext, UIUtils.dp2px(10));
            //只是绘制左上角和右上角圆角
            transformation.setExceptCorner(false, false, false, false);
            Glide.with(mContext)
                    .load(list.get(position).getCoverImage())
                    .asBitmap()
                    .skipMemoryCache(true)
                    .centerCrop()
                    .transform(transformation)
                    .into(imageView);
            imgList.setValueAt(position, imageView);
        }
        return imgList.valueAt(position);

    }
}