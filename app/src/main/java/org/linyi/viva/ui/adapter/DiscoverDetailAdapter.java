package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;

import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.BookDetailMayLikeHolder;
import org.linyi.viva.ui.viewholder.DiscoverDetailHolder1;
import org.linyi.viva.ui.viewholder.DiscoverDetailHolder2;

import java.util.List;

public class DiscoverDetailAdapter extends RecyclerView.Adapter<RecyclerViewBaseHolder> {
    List<BookApi.BookOverView> list;

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    private int currentType = 0;

    @NonNull
    @Override
    public RecyclerViewBaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        if (type == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_discover_recycler__item1, parent, false);
            return new DiscoverDetailHolder1(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_discover_recycler__item2, parent, false);
            return new DiscoverDetailHolder2(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return currentType;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewBaseHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == 0) {
            DiscoverDetailHolder1 holder1 = (DiscoverDetailHolder1) holder;
            holder1.setData(list.get(position));
        } else {
            DiscoverDetailHolder2 holder2 = (DiscoverDetailHolder2) holder;
            holder2.setData(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public void setData(List<BookApi.BookOverView> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
