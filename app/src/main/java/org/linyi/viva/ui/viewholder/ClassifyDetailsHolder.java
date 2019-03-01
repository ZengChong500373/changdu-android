package org.linyi.viva.ui.viewholder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;


import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;

public class ClassifyDetailsHolder extends RecyclerViewBaseHolder {
    private  ImageView img_cover;
    private   TextView tv_title,tv_details;
    public ClassifyDetailsHolder(View itemView) {
        super(itemView);
        img_cover=itemView.findViewById(R.id.img_cover);
        tv_title=itemView.findViewById(R.id.tv_title);
        tv_details=itemView.findViewById(R.id.tv_details);
    }
    public void setData(BookApi.AckCategoryList.Category data){
        ImageLoaderUtil.getInstance().loadImage(img_cover,data.getCoverImg());
        tv_title.setText(data.getName());
        tv_details.setText(data.getNum()+"本");
    }
}
