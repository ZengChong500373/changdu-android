package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;

import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;

import org.linyi.viva.ui.viewholder.DiscoverHolder;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverHolder> {
    private List<BookApi.AckBookThemeIntro.Data> list = new ArrayList<>();
    private ItemClickListener mListener;
    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public DiscoverHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_discover_adapter_item, parent, false);;
        return new DiscoverHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverHolder holder, int i) {
                   holder.setItemListener(mListener);
                   holder.setData(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 加载头部数据
     */
    public void loadHead(List<BookApi.AckBookThemeIntro.Data> data) {
        if (data == null) return;
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 加载底部数据
     */
    public void loadMore(List<BookApi.AckBookThemeIntro.Data> data) {
        if (data == null) return;
        list.addAll(data);
        notifyDataSetChanged();
    }
    public BookApi.AckBookThemeIntro.Data getPositionData(int position){
        return  list.get(position);
    }
}
