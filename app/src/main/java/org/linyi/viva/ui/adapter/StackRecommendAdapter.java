package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.api.changdu.proto.BookApi;

import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.StackRecommendHolder;

import java.util.List;


public class StackRecommendAdapter extends RecyclerView.Adapter<StackRecommendHolder> {
    private int position = 0;
   private int maxSize=0;
    private List<BookApi.BookOverView> allData;
    @NonNull
    @Override
    public StackRecommendHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_stack_recommend_item, parent, false);
        return new StackRecommendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StackRecommendHolder holder, int i) {
        int  currrentPosition=   4*position+i;
        if (currrentPosition>allData.size()-1){
            return;
        }
        holder.setData(allData.get(currrentPosition));
    }

    @Override
    public int getItemCount() {
        if (allData==null)
            return 0;
        return 4;
    }

    public void setData(BookApi.AckDomainRecommend.Data data) {
        position = 0;
        allData = data.getBookOverViewList();
        maxSize= (int) Math.ceil(allData.size()/4);
        notifyDataSetChanged();
    }

    public void forAnother() {
        if (position<maxSize){
            position++;
        }else {
            position=0;
        }
        notifyDataSetChanged();
    }
}
