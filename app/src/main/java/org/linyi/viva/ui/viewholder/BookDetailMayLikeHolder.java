package org.linyi.viva.ui.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;


import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;
import org.linyi.viva.ui.activity.BookDetailActivity;

public class BookDetailMayLikeHolder extends RecyclerViewBaseHolder {
    private ImageView img_cover;
    private TextView tv_title;
    private LinearLayout ll;

    public BookDetailMayLikeHolder(View itemView) {
        super(itemView);
        ll = itemView.findViewById(R.id.ll);
        img_cover = itemView.findViewById(R.id.img_cover);
        tv_title = itemView.findViewById(R.id.tv_title);

    }

    public void setData(final BookApi.AckBookDetail.SameBook data) {
        ViewGroup.LayoutParams params = img_cover.getLayoutParams();
        params.height = UIUtils.dp2px(106.5f);
        img_cover.setLayoutParams(params);
        ImageLoaderUtil.getInstance().loadImage(img_cover, data.getCoverImage());
        tv_title.setText(data.getTitle());
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ll.getContext(), BookDetailActivity.class);
                intent.putExtra("id", data.getBookID());
                ll.getContext().startActivity(intent);
            }
        });
    }
}
