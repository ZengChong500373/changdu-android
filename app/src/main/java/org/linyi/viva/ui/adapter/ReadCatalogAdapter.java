package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.api.changdu.proto.BookApi;
import com.linyi.reader.utils.NovelUtils;

import org.linyi.base.entity.db.HistoryRecordEntity;
import org.linyi.base.entity.params.ReaderParams;
import org.linyi.base.mvp.module.HistoryModule;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.TurnHelp;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;

import java.util.ArrayList;
import java.util.List;

public class ReadCatalogAdapter extends RecyclerView.Adapter<ReadCatalogHolder> {
    private List<BookApi.ChapterMsg> list = new ArrayList<>();
    /**
     * 0 正序
     * 1 倒序
     */
    private int type = 0;
    private String bookName,author,coverImge;
    private boolean isNigtht = NovelUtils.isNightMode();
    private int selectChapterID;
    private int selectPisition;

    @NonNull
    @Override
    public ReadCatalogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_read_catalog_recycler_item, parent, false);
        return new ReadCatalogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadCatalogHolder holder, int position) {
        BookApi.ChapterMsg data = null;
        if (type == 1) {
            data = list.get(position);
        } else {
            int index = list.size() - position - 1;
            data = list.get(index);
        }
        int index = list.indexOf(data);
        holder.setInfos(bookName,author,coverImge);
        holder.setData(data, index, isNigtht, selectChapterID);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<BookApi.ChapterMsg> data, int type) {
        if (data == null || data.size() == 0) {
            return;
        }
        this.type = type;
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public List<BookApi.ChapterMsg> getData() {
        return list;
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        selectPisition=position;
        selectChapterID = list.get(position).getChapterID();
    }
    public int getSetctedPosition(){
        if (type==1){
            return selectPisition;
        }
       return list.size() - selectPisition - 1;
    }

    public void setInfos(String bookName,String author,String coverImge) {
        this.bookName = bookName;
        this.author=author;
        this. coverImge=coverImge;
    }

    public void onResume() {
        isNigtht = NovelUtils.isNightMode();
        notifyDataSetChanged();
    }
}

class ReadCatalogHolder extends RecyclerViewBaseHolder {
    private RelativeLayout rl_parent;
    private TextView tv_title;
    private View view_line;
    private String bookName,author,coverImge;
    private ImageView img_lock;
    public ReadCatalogHolder(View itemView) {
        super(itemView);
        rl_parent = itemView.findViewById(R.id.rl_parent);
        tv_title = itemView.findViewById(R.id.tv_title);
        view_line=itemView.findViewById(R.id.view_line);
        img_lock=itemView.findViewById(R.id.img_lock);
    }
    public void setInfos(String bookName,String author,String coverImge) {
        this.bookName = bookName;
        this.author=author;
        this. coverImge=coverImge;
    }
    /**
     * index 当前章节的角标
     * isNigtht 是否是夜间模式
     * selectChapterID 选中的章节id
     */
    public void setData(final BookApi.ChapterMsg data, final int index, boolean isNigtht, int selectChapterID) {
        tv_title.setText(data.getChapterName());
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TurnHelp.reader(view.getContext(), new ReaderParams(data.getBookID(), bookName, index, 0));
//                HistoryRecordEntity entity=new HistoryRecordEntity(data.getBookID(),bookName,author,data.getChapterName(),coverImge);
//                HistoryRecordEntity entity=new HistoryRecordEntity(data.getBookID());
//                HistoryModule.save(entity);
            }
        });

       if (!isNigtht){
           rl_parent.setBackgroundColor(UIUtils.getColor(R.color.white));
           tv_title.setTextColor(UIUtils.getColor(R.color.text_color));
           view_line.setBackgroundColor(UIUtils.getColor(R.color.line_color2));
       }else {
           rl_parent.setBackgroundColor(UIUtils.getColor(R.color.read_bg_6));
           tv_title.setTextColor(UIUtils.getColor(R.color.read_font_6));
           view_line.setBackgroundColor(UIUtils.getColor(R.color.read_font_6));
       }
       if (data.getChapterID()==selectChapterID){
           tv_title.setTextColor(UIUtils.getColor(R.color.text_color_orange));
       }
if (data.getPayType()==1){
    img_lock.setVisibility(View.GONE);
}else {
    img_lock.setVisibility(View.VISIBLE);
}
    }
}
