package org.linyi.viva.ui.viewholder;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;

import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;
import org.linyi.viva.ui.activity.BookDetailActivity;

public class DiscoverDetailHolder1 extends RecyclerViewBaseHolder {
    private ImageView img_cover;
    private  TextView tv_title,tv_kinds,tv_description;
    private RelativeLayout rl;
    public DiscoverDetailHolder1(View itemView) {
        super(itemView);
        img_cover=itemView.findViewById(R.id.img_cover);
        tv_title=itemView.findViewById(R.id.tv_title);
        tv_kinds=itemView.findViewById(R.id.tv_kinds);
        tv_description=itemView.findViewById(R.id.tv_description);
        rl=itemView.findViewById(R.id.rl);
    }
    public void setData(final BookApi.BookOverView data){
        ViewGroup.LayoutParams params = img_cover.getLayoutParams();
        params.width = UIUtils.dp2px(70f);
        params.height = UIUtils.dp2px(103.5f);
        img_cover.setLayoutParams(params);
        ImageLoaderUtil.getInstance().loadImage(img_cover, data.getCoverImage());
        tv_title.setText(data.getTitle());
        String kinds=data.getCategoryName()+"."+data.getWordsTotal()+"字";
        tv_kinds.setText(kinds);
        tv_description.setText(data.getDescription());
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rl.getContext(), BookDetailActivity.class);
                intent.putExtra("id", data.getBookID());
                rl.getContext().startActivity(intent);
            }
        });
    }
}
