package org.linyi.viva.ui.viewholder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.api.changdu.proto.BookApi;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;

public class DiscoverHolder extends RecyclerViewBaseHolder {
    private ImageView img_cover;
    private  TextView tv_title;
    public DiscoverHolder(View itemView) {
        super(itemView);
        img_cover=itemView.findViewById(R.id.img_cover);
        tv_title=itemView.findViewById(R.id.tv_title);

    }
    public void setData(BookApi.AckBookThemeIntro.Data data){

        ImageLoaderUtil.getInstance().loadImage(img_cover,data.getCoverImage());
        tv_title.setText(data.getTitle());


    }
}
