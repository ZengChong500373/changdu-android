package org.linyi.viva.ui.viewholder;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;


import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;

public class Classify2ListHolder  extends RecyclerViewBaseHolder {
    private ImageView img_cover;
    private  TextView tv_title,tv_author,tv_read;
    public Classify2ListHolder(View itemView) {
        super(itemView);
        img_cover=itemView.findViewById(R.id.img_cover);
        tv_title=itemView.findViewById(R.id.tv_title);
        tv_author=itemView.findViewById(R.id.tv_author);
        tv_read=itemView.findViewById(R.id.tv_read);
    }
    public void setData(BookApi.BookOverView data){
        ViewGroup.LayoutParams params=  img_cover.getLayoutParams();
        params.width=UIUtils.dp2px(59);
        params.height= UIUtils.dp2px(78.5f);
        img_cover.setLayoutParams(params);
        ImageLoaderUtil.getInstance().loadImage(img_cover,data.getCoverImage());
        tv_title.setText(data.getTitle());
        tv_author.setText("作者:"+data.getAuthorName());
        String str=data.getReadTotal()+"浏览"+" "+data.getWordsTotal()+"字";
        tv_read.setText(str);

    }
}
