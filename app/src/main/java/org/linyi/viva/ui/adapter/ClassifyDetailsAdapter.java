package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;

import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.ClassifyDetailsHolder;



public class ClassifyDetailsAdapter extends RecyclerView.Adapter<ClassifyDetailsHolder>{
    private ItemClickListener mListener;
    public BookApi.AckCategoryList.Rank data;
    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public ClassifyDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_classify_recycler_details_item, parent, false);;
        return new ClassifyDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyDetailsHolder holder, int i) {
            holder.setItemListener(mListener);
          BookApi.AckCategoryList.Category category=   data.getCategoryList().get(i);
          holder.setData(category);
    }

    @Override
    public int getItemCount() {
        if (data==null||data.getCategoryList()==null||data.getCategoryList().size()==0){
            return 0;
        }
       return data.getCategoryList().size();
    }
    public void setData(BookApi.AckCategoryList.Rank data){
        this.data=data;
        notifyDataSetChanged();
    }
    public String getName(int position){
        return data.getCategoryList().get(position).getName();
    }

}
