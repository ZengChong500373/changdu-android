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

public class RankTypeAdapter extends RecyclerView.Adapter<ClassifyTypeHolder> {
    private BookApi.AckIndexRankingList  allData;
    private BookApi.AckIndexRankingList.Site currentData ;
    private ItemClickListener mListener;
    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public ClassifyTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rank_recycler_type_item, parent, false);;
        return new ClassifyTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyTypeHolder holder, int position) {
            String name=currentData.getRank(position).getName();
            holder.setItemListener(mListener);
            if (position==selected_position){
                holder.setData(name,true);
            }else {
                holder.setData(name,false);
            }
    }

    @Override
    public int getItemCount() {
        if (currentData!=null&&currentData.getRankList()!=null){
            return currentData.getRankList().size();
        }
        return 0;
    }
    public void setData(BookApi.AckIndexRankingList  data ){
        this.allData=data;
    }
    public int selected_position=-1;
   public void selected(int position){
      this. selected_position=position;
      notifyDataSetChanged();
   }
   public void switchType(int position){
       currentData=allData.getSite(position);
       notifyDataSetChanged();
   }
   public String getSelectedName(){
       if (selected_position==-1){
           return "";
       }
       return currentData.getRank(selected_position).getName();
   }

}
