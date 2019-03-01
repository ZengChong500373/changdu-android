package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.BookDetailMayLikeHolder;
import java.util.List;

public class BookDetailMayLikeAdapter extends RecyclerView.Adapter<BookDetailMayLikeHolder>{
    List<BookApi.AckBookDetail.SameBook> list;

    @NonNull
    @Override
    public BookDetailMayLikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_detail_recycler_item, parent, false);
        return new BookDetailMayLikeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookDetailMayLikeHolder holder, int i) {
          holder.setData(list.get(i));
    }

    @Override
    public int getItemCount() {
        if (list==null)
        return 0;
        return list.size();
    }
    public void setData(List<BookApi.AckBookDetail.SameBook> list){
        this.list=list;
        notifyDataSetChanged();
    }

}
