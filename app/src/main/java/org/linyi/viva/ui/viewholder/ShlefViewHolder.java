package org.linyi.viva.ui.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.api.changdu.proto.BookApi;
import com.bumptech.glide.Glide;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;
import org.linyi.viva.ui.adapter.ShelFragmentAdapter;

public class ShlefViewHolder extends RecyclerViewBaseHolder {
    private ImageView img_book_cover;
    private TextView tv_book_name;
    private TextView tv_book_describe;
    private View book_status;
    private ImageView img_select_status;
    private int showType;
    private boolean isSelect=false;
    private BookApi.AckMyBookShelf.Data data;
    private RelativeLayout rl;
    public ShlefViewHolder(View itemView) {
        super(itemView);
        img_book_cover = itemView.findViewById(R.id.img_book_cover);
        tv_book_name = itemView.findViewById(R.id.tv_book_name);
        tv_book_describe = itemView.findViewById(R.id.tv_book_describe);
        book_status=itemView.findViewById(R.id.book_status);
        img_select_status=itemView.findViewById(R.id.img_select_status);
        rl=itemView.findViewById(R.id.rl);
    }
    public void setData(BookApi.AckMyBookShelf.Data data,int showType) {
        this.showType=showType;
        this.data=data;
        ImageLoaderUtil.getInstance().loadImage(img_book_cover,data.getBookOverView().getCoverImage());
        tv_book_name.setText(data.getBookOverView().getTitle());
        tv_book_describe.setText(data.getBookOverView().getLastChapterUpdateTime()+":"+data.getBookOverView().getLastChapterTitle());
        if (data.getIsTop()|| SharePreUtil.getBoolean(data.getBookOverView().getBookID(),false)){
            rl.setBackgroundColor(Color.parseColor("#ffeeeeee"));
        }else {
            rl.setBackgroundColor(UIUtils.getColor(R.color.white));
        }
        if (showType== ShelFragmentAdapter.commonType){
            book_status.setVisibility(View.VISIBLE);
            img_select_status.setVisibility(View.GONE);
            Glide.with(img_select_status.getContext()).load(R.drawable.main_shelf_delect_normal).into(img_select_status);
        }else if (showType== ShelFragmentAdapter.deleType){
            setStatus(false);
        }else{
            setStatus(true);
        }
        String localLastChater=   SharePreUtil.getString(data.getBookOverView().getBookID()+"_LastChapterTitle");
        String netlastChater=data.getBookOverView().getLastChapterTitle();
        if (!netlastChater.equals(localLastChater)){
            book_status.setVisibility(View.VISIBLE);
        }else {
            book_status.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (showType==ShelFragmentAdapter.commonType) {
            super.onClick(view);
            return;
        }
        if (isSelect){
            setStatus(false);
        }else {
            setStatus(true);
        }
    }
    public void setStatus(boolean select){
        isSelect=select;
         if (book_status.getVisibility()==View.VISIBLE){
             book_status.setVisibility(View.GONE);
         }
        if (img_select_status.getVisibility()==View.GONE){
            img_select_status.setVisibility(View.VISIBLE);
        }
        if (select){
            Glide.with(img_select_status.getContext()).load(R.drawable.main_shelf_delect_selected).into(img_select_status);
            ShelFragmentAdapter.putMapData(data);
        }else {
            Glide.with(img_select_status.getContext()).load(R.drawable.main_shelf_delect_normal).into(img_select_status);
            ShelFragmentAdapter.removeMapData(data);
        }
    }
}
