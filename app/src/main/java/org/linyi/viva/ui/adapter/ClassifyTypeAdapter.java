package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;

import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.ClassifyTypeHolder;

public class ClassifyTypeAdapter extends RecyclerView.Adapter<ClassifyTypeHolder> {
    private BookApi.AckCategoryList data;
    private ItemClickListener mListener;

    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ClassifyTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rank_recycler_type_item, parent, false);
        ;
        return new ClassifyTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyTypeHolder holder, int position) {
        String name = data.getRank(position).getName();
        holder.setItemListener(mListener);
        if (position == selected_position) {
            holder.setData(name, true);
        } else {
            holder.setData(name, false);
        }
    }

    @Override
    public int getItemCount() {
        if (data != null && data.getRankList() != null) {
            return data.getRankList().size();
        }
        return 0;
    }

    public void setData(BookApi.AckCategoryList data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public BookApi.AckCategoryList.Rank getData(int position) {
        return data.getRank(position);
    }

    public int selected_position = -1;

    public void selected(int position) {
        this.selected_position = position;
        notifyDataSetChanged();
    }
}
