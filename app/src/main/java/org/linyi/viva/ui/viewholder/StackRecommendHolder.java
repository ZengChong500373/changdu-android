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


public class StackRecommendHolder extends RecyclerViewBaseHolder {
    private    ImageView img_stack_book_cover;
    private  TextView tv_book_name;
    private LinearLayout ll;
    public StackRecommendHolder(View itemView) {
        super(itemView);
        img_stack_book_cover=itemView.findViewById(R.id.img_stack_book_cover);
        tv_book_name=itemView.findViewById(R.id.tv_book_name);
        ll=itemView.findViewById(R.id.ll);
    }
    public void setData(final BookApi.BookOverView info){
        ViewGroup.LayoutParams params=  img_stack_book_cover.getLayoutParams();
        int viewW=((UIUtils.getWindowWidth()-UIUtils.dp2px(60))/4);
        params.height= (int) (viewW*1.33333);
        img_stack_book_cover.setLayoutParams(params);
        tv_book_name.setText(info.getTitle());
        ImageLoaderUtil.getInstance().loadImage(img_stack_book_cover,info.getCoverImage());
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ll.getContext(), BookDetailActivity.class);
                intent.putExtra("id",   info.getBookID());
                ll.getContext().startActivity(intent);
            }
        });

    }

}
