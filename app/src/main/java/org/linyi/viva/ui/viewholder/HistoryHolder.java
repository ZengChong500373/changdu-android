package org.linyi.viva.ui.viewholder;


import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.api.changdu.proto.BookApi;
import com.google.protobuf.InvalidProtocolBufferException;

import org.linyi.base.constant.SystemConstant;
import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.base.entity.db.HistoryRecordEntity;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;

import org.linyi.base.utils.UIUtils;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;

import java.io.UnsupportedEncodingException;


public class HistoryHolder extends RecyclerViewBaseHolder {
    private ImageView img_cover;
    private TextView tv_title,tv_read_chapter,tv_author;


    private ImageView img_select_status;

private  HistoryRecordDbEntity currentData;

    public HistoryHolder(View itemView) {
        super(itemView);
        img_cover = itemView.findViewById(R.id.img_cover);
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_read_chapter = itemView.findViewById(R.id.tv_read_chapter);
        tv_author=itemView.findViewById(R.id.tv_author);
        img_select_status=itemView.findViewById(R.id.img_select_status);

    }
    public void setData(HistoryRecordDbEntity data) {
        currentData=data;
        ViewGroup.LayoutParams params=  img_cover.getLayoutParams();
        params.width=UIUtils.dp2px(59);
        params.height= UIUtils.dp2px(78.5f);
        img_cover.setLayoutParams(params);
        String detailStr=data.getBookEntity().getDetailStr();
        if (TextUtils.isEmpty(detailStr)){
            return;
        }
        BookApi.BookOverView book = null;
        try {
            book = BookApi.BookOverView.parseFrom(detailStr.getBytes(SystemConstant.CHARSET_NAME));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ImageLoaderUtil.getInstance().loadImage(img_cover,book.getCoverImage());

        tv_title.setText(book.getTitle());

        tv_read_chapter.setText("阅读到"+data.getTime());
        tv_author.setText("作者:"+book.getAuthorName());

    }

    @Override
    public void onClick(View view) {
        
}
    public void setStatus(boolean select){

    }
}
