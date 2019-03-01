package org.linyi.viva.ui.viewholder;


import android.content.Intent;
import android.util.Log;
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

public class DiscoverDetailHolder2 extends RecyclerViewBaseHolder {
    private ImageView img_cover;
    private  TextView tv_book_name,tv_author;
    private LinearLayout ll;
    public DiscoverDetailHolder2(View itemView) {
        super(itemView);
        img_cover=itemView.findViewById(R.id.img_cover);
        tv_book_name=itemView.findViewById(R.id.tv_book_name);
        tv_author=itemView.findViewById(R.id.tv_author);
        ll=itemView.findViewById(R.id.ll);
    }
    public void setData(final BookApi.BookOverView data){
        ViewGroup.LayoutParams params = img_cover.getLayoutParams();
        params.height = UIUtils.dp2px(103.5f);
        img_cover.setLayoutParams(params);
        ImageLoaderUtil.getInstance().loadImage(img_cover, data.getCoverImage());
        tv_book_name.setText(data.getTitle());
        tv_author.setText(data.getAuthorName());

        ll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ll.getContext(), BookDetailActivity.class);
               intent.putExtra("id", data.getBookID());
               ll.getContext().startActivity(intent);
           }
       });
    }
}
