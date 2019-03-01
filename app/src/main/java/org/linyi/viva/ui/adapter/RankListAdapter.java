package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.RankListHolder;

import java.util.ArrayList;
import java.util.List;

public class RankListAdapter extends RecyclerView.Adapter<RankListHolder> {

    private List<BookApi.BookOverView> list = new ArrayList<>();

    @NonNull
    @Override
    public RankListHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rank_recycler_list_item, parent, false);
        return new RankListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankListHolder holder, int position) {
        BookApi.BookOverView data=list.get(position);
        holder.setData(data,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 加载头部数据
     */
    public void loadHead(BookApi.AckIndexRankingDetail data) {
        if (data==null||data.getBookOverViewList()==null){
           return;
        }
        list.clear();
        list.addAll(data.getBookOverViewList());
        notifyDataSetChanged();
    }

    /**
     * 加载底部数据
     */
    public void loadMore(BookApi.AckIndexRankingDetail data) {
        if (data==null||data.getBookOverViewList()==null){
            return;
        }
        list.addAll(data.getBookOverViewList());
        notifyDataSetChanged();
    }

}
