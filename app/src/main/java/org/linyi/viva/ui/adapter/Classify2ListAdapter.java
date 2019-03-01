package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;

import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.Classify2ListHolder;


import java.util.ArrayList;
import java.util.List;

public class Classify2ListAdapter extends RecyclerView.Adapter<Classify2ListHolder> {
    private List<BookApi.BookOverView> list = new ArrayList<>();
    private ItemClickListener mListener;
    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public Classify2ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_classify2_list_item, parent, false);;
        return new Classify2ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Classify2ListHolder holder, int i) {
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
    public void loadHead(List<BookApi.BookOverView> data) {
        if (data == null) return;
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 加载底部数据
     */
    public void loadMore(List<BookApi.BookOverView> data) {
        if (data == null) return;
        list.addAll(data);
        notifyDataSetChanged();
    }
    public BookApi.BookOverView getPositionData(int position){
        return  list.get(position);
    }
}
